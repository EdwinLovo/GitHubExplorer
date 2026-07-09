package edwinlovo.githubexplorer.presentation.ux.favorites

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.repository.fakes.FakeFavoritesRepository
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesEvent
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val favoritesRepository = FakeFavoritesRepository()

    private var viewModel: FavoritesViewModel? = null

    @After
    fun tearDown() {
        viewModel?.viewModelScope?.cancel()
    }

    @Test
    fun initCollectsFavoritesAndClearsLoading() = runTest(mainDispatcherRule.dispatcher) {
        favoritesRepository.addFavorite(sampleFavorite(id = 1L))
        val vm = buildViewModel()

        assertThat(vm.uiState.value.isLoading).isFalse()
        assertThat(vm.uiState.value.favorites).containsExactly(sampleFavorite(id = 1L))
    }

    @Test
    fun favoritesUpdateWhenRepositoryChanges() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()
        assertThat(vm.uiState.value.favorites).isEmpty()

        favoritesRepository.addFavorite(sampleFavorite(id = 1L))
        assertThat(vm.uiState.value.favorites).hasSize(1)

        favoritesRepository.removeFavorite(1L)
        assertThat(vm.uiState.value.favorites).isEmpty()
    }

    @Test
    fun onFavoriteDismissedRemovesFavorite() = runTest(mainDispatcherRule.dispatcher) {
        val favorite = sampleFavorite(id = 7L)
        favoritesRepository.addFavorite(favorite)
        val vm = buildViewModel()

        vm.handleEvent(FavoritesEvent.OnFavoriteDismissed(favorite))

        assertThat(favoritesRepository.removeCalls).containsExactly(7L)
        assertThat(vm.uiState.value.favorites).isEmpty()
    }

    @Test
    fun onRepoClickedEmitsNavigateToRepoDetail() = runTest(mainDispatcherRule.dispatcher) {
        val favorite = sampleFavorite(id = 1L, name = "kotlin", ownerLogin = "JetBrains")
        val vm = buildViewModel()

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(FavoritesEvent.OnRepoClicked(favorite))
            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.Navigate(RepoDetailRoute(owner = "JetBrains", repo = "kotlin"))
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun buildViewModel(): FavoritesViewModel =
        FavoritesViewModel(favoritesRepository).also { viewModel = it }

    private fun sampleFavorite(
        id: Long,
        name: String = "repo-$id",
        ownerLogin: String = "owner-$id",
    ): FavoriteRepo = FavoriteRepo(
        id = id,
        name = name,
        ownerLogin = ownerLogin,
        ownerAvatarUrl = "https://a.example/$id.png",
        description = "desc",
        stargazersCount = 10,
        language = "Kotlin",
    )
}
