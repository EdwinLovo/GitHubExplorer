package edwinlovo.githubexplorer.presentation.ux.main

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.presentation.ui.navigation.BottomNavTab
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.ux.users.UsersRoute
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    @Test
    fun navigateToTabEmitsNavigateToTabAction() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()

            viewModel.navigateToTab(BottomNavTab.Users.route)

            assertThat(awaitItem()).isEqualTo(NavigationAction.NavigateToTab(UsersRoute))

            cancelAndIgnoreRemainingEvents()
        }
    }
}
