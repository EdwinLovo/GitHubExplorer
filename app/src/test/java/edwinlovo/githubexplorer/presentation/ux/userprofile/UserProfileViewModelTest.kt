package edwinlovo.githubexplorer.presentation.ux.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.repository.fakes.FakeUserRepository
import edwinlovo.githubexplorer.data.repository.fakes.FakeUserRepository.Companion.sampleProfile
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileEvent
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val userRepository = FakeUserRepository()

    private var viewModel: UserProfileViewModel? = null

    @After
    fun tearDown() {
        viewModel?.viewModelScope?.cancel()
    }

    @Test
    fun loadPopulatesStateOnSuccess() = runTest(mainDispatcherRule.dispatcher) {
        val profile = sampleProfile(login = "torvalds")
        userRepository.setSuccess(profile)
        val vm = buildViewModel(username = "torvalds")

        assertThat(userRepository.requests).containsExactly("torvalds")
        assertThat(vm.uiState.value.isLoading).isFalse()
        assertThat(vm.uiState.value.user).isEqualTo(profile)
        assertThat(vm.uiState.value.hasError).isFalse()
    }

    @Test
    fun loadSetsRateLimitedFlagOn403() = runTest(mainDispatcherRule.dispatcher) {
        userRepository.setError(code = 403)
        val vm = buildViewModel()

        assertThat(vm.uiState.value.hasError).isTrue()
        assertThat(vm.uiState.value.isRateLimited).isTrue()
    }

    @Test
    fun loadSetsRateLimitedFlagOn429() = runTest(mainDispatcherRule.dispatcher) {
        userRepository.setError(code = 429)
        val vm = buildViewModel()

        assertThat(vm.uiState.value.hasError).isTrue()
        assertThat(vm.uiState.value.isRateLimited).isTrue()
    }

    @Test
    fun loadSetsErrorWithoutRateLimitFlagOn500() = runTest(mainDispatcherRule.dispatcher) {
        userRepository.setError(code = 500)
        val vm = buildViewModel()

        assertThat(vm.uiState.value.hasError).isTrue()
        assertThat(vm.uiState.value.isRateLimited).isFalse()
    }

    @Test
    fun onRetryReloadsRepository() = runTest(mainDispatcherRule.dispatcher) {
        userRepository.setError(code = 500)
        val vm = buildViewModel()
        assertThat(userRepository.requests).hasSize(1)

        userRepository.setSuccess(sampleProfile())
        vm.handleEvent(UserProfileEvent.OnRetryClicked)

        assertThat(userRepository.requests).hasSize(2)
        assertThat(vm.uiState.value.user).isNotNull()
        assertThat(vm.uiState.value.hasError).isFalse()
    }

    @Test
    fun onBackClickedEmitsPopBackStack() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(UserProfileEvent.OnBackClicked)
            val action = awaitItem()
            assertThat(action).isInstanceOf(NavigationAction.PopBackStack::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onRepoClickedEmitsNavigateToRepoDetail() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()
        val repo = GithubRepo(1L, "linux", "torvalds/linux", "torvalds", "url", "desc", 42, "C")

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(UserProfileEvent.OnRepoClicked(repo))
            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.Navigate(RepoDetailRoute(owner = "torvalds", repo = "linux"))
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun buildViewModel(username: String = "torvalds"): UserProfileViewModel {
        val savedStateHandle = SavedStateHandle().apply { set("username", username) }
        return UserProfileViewModel(savedStateHandle, userRepository).also { viewModel = it }
    }
}
