package edwinlovo.githubexplorer.presentation.ux.users

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.repository.fakes.FakeSearchRepository
import edwinlovo.githubexplorer.domain.model.response.search.AccountType
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.utils.DEBOUNCE_TIME
import edwinlovo.githubexplorer.presentation.ux.userprofile.UserProfileRoute
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersEvent
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
class UsersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private lateinit var repository: FakeSearchRepository
    private lateinit var viewModel: UsersViewModel

    @Before
    fun setUp() {
        repository = FakeSearchRepository(users = sampleUsers())
        viewModel = UsersViewModel(repository)
    }

    @After
    fun tearDown() {
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun handleEventOnSearchQueryChangedUpdatesUiState() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.uiState.test {
            assertThat(awaitItem().searchQuery).isEmpty()

            viewModel.handleEvent(UsersEvent.OnSearchQueryChanged("torvalds"))

            assertThat(awaitItem().searchQuery).isEqualTo("torvalds")
        }
    }

    @Test
    fun handleEventOnClearSearchQueryResetsQuery() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.handleEvent(UsersEvent.OnSearchQueryChanged("torvalds"))
        viewModel.handleEvent(UsersEvent.OnClearSearchQuery)

        assertThat(viewModel.uiState.value.searchQuery).isEmpty()
    }

    @Test
    fun usersBlankQueryDoesNotHitRepository() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.users.collect {} }
        advanceUntilIdle()

        assertThat(repository.requestedUserQueries).isEmpty()
    }

    @Test
    fun usersDebouncesNonBlankQuery() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.users.collect {} }
        advanceUntilIdle()
        assertThat(repository.requestedUserQueries).isEmpty()

        viewModel.handleEvent(UsersEvent.OnSearchQueryChanged("t"))
        advanceTimeBy(DEBOUNCE_TIME - 1)
        assertThat(repository.requestedUserQueries).isEmpty()

        advanceTimeBy(2)
        advanceUntilIdle()
        assertThat(repository.requestedUserQueries).contains("t")
    }

    @Test
    fun usersCollapsesRapidQueryChangesToLastQuery() = runTest(mainDispatcherRule.dispatcher) {
        backgroundScope.launch { viewModel.users.collect {} }
        advanceUntilIdle()

        viewModel.handleEvent(UsersEvent.OnSearchQueryChanged("t"))
        advanceTimeBy(100)
        viewModel.handleEvent(UsersEvent.OnSearchQueryChanged("to"))
        advanceTimeBy(100)
        viewModel.handleEvent(UsersEvent.OnSearchQueryChanged("tor"))
        advanceTimeBy(DEBOUNCE_TIME + 1)
        advanceUntilIdle()

        val queries = repository.requestedUserQueries
        assertThat(queries).contains("tor")
        assertThat(queries).doesNotContain("t")
        assertThat(queries).doesNotContain("to")
    }

    @Test
    fun handleEventOnUserClickedEmitsNavigateToUserProfile() = runTest(mainDispatcherRule.dispatcher) {
        val user = GithubUser(1L, "torvalds", "url", AccountType.USER)

        viewModel.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()

            viewModel.handleEvent(UsersEvent.OnUserClicked(user))

            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.Navigate(UserProfileRoute(username = "torvalds"))
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun sampleUsers(): List<GithubUser> = listOf(
        GithubUser(1L, "torvalds", "url", AccountType.USER),
        GithubUser(2L, "google", "url", AccountType.ORGANIZATION),
    )
}
