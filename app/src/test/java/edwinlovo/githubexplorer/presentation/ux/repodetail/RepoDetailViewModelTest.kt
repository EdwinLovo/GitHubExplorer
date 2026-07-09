package edwinlovo.githubexplorer.presentation.ux.repodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.repository.fakes.FakeFavoritesRepository
import edwinlovo.githubexplorer.data.repository.fakes.FakeRepoRepository
import edwinlovo.githubexplorer.data.repository.fakes.FakeRepoRepository.Companion.sampleDetails
import edwinlovo.githubexplorer.domain.model.favorites.toFavoriteRepo
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.ux.repodetail.contracts.RepoDetailEvent
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val repoRepository = FakeRepoRepository()
    private val favoritesRepository = FakeFavoritesRepository()

    private var viewModel: RepoDetailViewModel? = null

    @After
    fun tearDown() {
        viewModel?.viewModelScope?.cancel()
    }

    @Test
    fun loadPopulatesStateOnSuccess() = runTest(mainDispatcherRule.dispatcher) {
        val details = sampleDetails(name = "compose", ownerLogin = "google")
        repoRepository.setSuccess(details)
        val vm = buildViewModel(owner = "google", repo = "compose")

        assertThat(repoRepository.requests).containsExactly("google" to "compose")
        assertThat(vm.uiState.value.isLoading).isFalse()
        assertThat(vm.uiState.value.repo).isEqualTo(details)
        assertThat(vm.uiState.value.hasError).isFalse()
    }

    @Test
    fun loadSetsRateLimitedFlagOn403() = runTest(mainDispatcherRule.dispatcher) {
        repoRepository.setError(code = 403)
        val vm = buildViewModel()

        assertThat(vm.uiState.value.hasError).isTrue()
        assertThat(vm.uiState.value.isRateLimited).isTrue()
    }

    @Test
    fun loadSetsRateLimitedFlagOn429() = runTest(mainDispatcherRule.dispatcher) {
        repoRepository.setError(code = 429)
        val vm = buildViewModel()

        assertThat(vm.uiState.value.hasError).isTrue()
        assertThat(vm.uiState.value.isRateLimited).isTrue()
    }

    @Test
    fun loadSetsErrorWithoutRateLimitFlagOn500() = runTest(mainDispatcherRule.dispatcher) {
        repoRepository.setError(code = 500)
        val vm = buildViewModel()

        assertThat(vm.uiState.value.hasError).isTrue()
        assertThat(vm.uiState.value.isRateLimited).isFalse()
    }

    @Test
    fun onRetryReloadsRepository() = runTest(mainDispatcherRule.dispatcher) {
        repoRepository.setError(code = 500)
        val vm = buildViewModel()
        assertThat(repoRepository.requests).hasSize(1)

        repoRepository.setSuccess(sampleDetails())
        vm.handleEvent(RepoDetailEvent.OnRetryClicked)

        assertThat(repoRepository.requests).hasSize(2)
        assertThat(vm.uiState.value.repo).isNotNull()
        assertThat(vm.uiState.value.hasError).isFalse()
    }

    @Test
    fun onFavoriteToggledAddsWhenNotFavorite() = runTest(mainDispatcherRule.dispatcher) {
        val details = sampleDetails()
        repoRepository.setSuccess(details)
        val vm = buildViewModel(owner = details.ownerLogin, repo = details.name)

        vm.handleEvent(RepoDetailEvent.OnFavoriteToggled)

        assertThat(favoritesRepository.addCalls).containsExactly(details.toFavoriteRepo())
        assertThat(vm.uiState.value.isFavorite).isTrue()
    }

    @Test
    fun onFavoriteToggledRemovesWhenAlreadyFavorite() = runTest(mainDispatcherRule.dispatcher) {
        val details = sampleDetails()
        repoRepository.setSuccess(details)
        favoritesRepository.addFavorite(details.toFavoriteRepo())
        val vm = buildViewModel(owner = details.ownerLogin, repo = details.name)

        assertThat(vm.uiState.value.isFavorite).isTrue()

        vm.handleEvent(RepoDetailEvent.OnFavoriteToggled)

        assertThat(favoritesRepository.removeCalls).containsExactly(details.id)
        assertThat(vm.uiState.value.isFavorite).isFalse()
    }

    @Test
    fun onBackClickedEmitsPopBackStack() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(RepoDetailEvent.OnBackClicked)
            val action = awaitItem()
            assertThat(action).isInstanceOf(NavigationAction.PopBackStack::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun buildViewModel(
        owner: String = "google",
        repo: String = "compose",
    ): RepoDetailViewModel {
        val savedStateHandle = SavedStateHandle().apply {
            set("owner", owner)
            set("repo", repo)
        }
        return RepoDetailViewModel(savedStateHandle, repoRepository, favoritesRepository)
            .also { viewModel = it }
    }
}
