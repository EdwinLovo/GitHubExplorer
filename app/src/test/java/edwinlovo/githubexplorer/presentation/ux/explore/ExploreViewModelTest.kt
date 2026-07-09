package edwinlovo.githubexplorer.presentation.ux.explore

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.repository.fakes.FakeSearchRepository
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.utils.DEBOUNCE_TIME
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreEvent
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import edwinlovo.githubexplorer.presentation.ux.searchfilters.SearchFiltersRoute
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private lateinit var repository: FakeSearchRepository
    private lateinit var viewModel: ExploreViewModel

    @Before
    fun setUp() {
        repository = FakeSearchRepository(repos = sampleRepos())
        viewModel = ExploreViewModel(repository)
    }

    @After
    fun tearDown() {
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun handleEventOnSearchQueryChangedUpdatesUiState() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.uiState.test {
            assertThat(awaitItem().searchQuery).isEmpty()

            viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("compose"))

            assertThat(awaitItem().searchQuery).isEqualTo("compose")
        }
    }

    @Test
    fun handleEventOnClearSearchQueryResetsQuery() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("compose"))
        viewModel.handleEvent(ExploreEvent.OnClearSearchQuery)

        assertThat(viewModel.uiState.value.searchQuery).isEmpty()
    }

    @Test
    fun reposRequestsBlankQueryImmediately() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.repos.collect {} }
        advanceUntilIdle()

        assertThat(repository.requestedQueries).containsExactly("")
    }

    @Test
    fun reposDebouncesNonBlankQuery() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.repos.collect {} }
        advanceUntilIdle()
        assertThat(repository.requestedQueries).containsExactly("")

        viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("c"))
        advanceTimeBy(DEBOUNCE_TIME - 1)
        assertThat(repository.requestedQueries).containsExactly("")

        advanceTimeBy(2)
        advanceUntilIdle()
        assertThat(repository.requestedQueries).contains("c")
    }

    @Test
    fun reposCollapsesRapidQueryChangesToLastQuery() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.repos.collect {} }
        advanceUntilIdle()

        viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("a"))
        advanceTimeBy(100)
        viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("ab"))
        advanceTimeBy(100)
        viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("abc"))
        advanceTimeBy(DEBOUNCE_TIME + 1)
        advanceUntilIdle()

        val queries = repository.requestedQueries
        assertThat(queries).contains("abc")
        assertThat(queries).doesNotContain("a")
        assertThat(queries).doesNotContain("ab")
    }

    @Test
    fun handleEventOnRepoClickedEmitsNavigateToRepoDetail() = runTest(mainDispatcherRule.dispatcher) {
        val repo = GithubRepo(1L, "compose", "google/compose", "google", "url", "desc", 12, "Kotlin")

        viewModel.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()

            viewModel.handleEvent(ExploreEvent.OnRepoClicked(repo))

            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.Navigate(RepoDetailRoute(owner = "google", repo = "compose"))
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun handleEventOnFilterClickedEmitsNavigateToSearchFilters() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()

            viewModel.handleEvent(ExploreEvent.OnFilterClicked)

            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.Navigate(SearchFiltersRoute(language = ""))
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun handleEventOnLanguageFilterResultSetsFilterAndSnackbarFlag() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.handleEvent(ExploreEvent.OnLanguageFilterResult("kotlin"))

        assertThat(viewModel.uiState.value.languageFilter).isEqualTo("kotlin")
        assertThat(viewModel.uiState.value.showFilterSnackbar).isTrue()
    }

    @Test
    fun handleEventOnFilterSnackbarShownClearsFlag() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.handleEvent(ExploreEvent.OnLanguageFilterResult("kotlin"))
        viewModel.handleEvent(ExploreEvent.OnFilterSnackbarShown)

        assertThat(viewModel.uiState.value.showFilterSnackbar).isFalse()
    }

    @Test
    fun reposAppendsLanguageQualifierToNonBlankQuery() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.repos.collect {} }
        advanceUntilIdle()

        viewModel.handleEvent(ExploreEvent.OnLanguageFilterResult("kotlin"))
        viewModel.handleEvent(ExploreEvent.OnSearchQueryChanged("http"))
        advanceTimeBy(DEBOUNCE_TIME + 1)
        advanceUntilIdle()

        assertThat(repository.requestedQueries).contains("http language:kotlin")
    }

    @Test
    fun reposRequestsLanguageOnlyQueryWhenSearchBlank() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.repos.collect {} }
        advanceUntilIdle()

        viewModel.handleEvent(ExploreEvent.OnLanguageFilterResult("kotlin"))
        advanceUntilIdle()

        assertThat(repository.requestedQueries).contains("language:kotlin")
    }

    private fun sampleRepos(): List<GithubRepo> = listOf(
        GithubRepo(1L, "kotlin", "JetBrains/kotlin", "JetBrains", "url", "desc", 42, "Kotlin"),
        GithubRepo(2L, "compose", "google/compose", "google", "url", "desc", 12, "Kotlin"),
    )
}
