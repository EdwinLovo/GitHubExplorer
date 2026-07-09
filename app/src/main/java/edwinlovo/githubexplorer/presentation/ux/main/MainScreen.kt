package edwinlovo.githubexplorer.presentation.ux.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import edwinlovo.githubexplorer.presentation.ui.components.dialogs.GitHubExplorerErrorAlertDialog
import edwinlovo.githubexplorer.presentation.ui.components.navigation.GheBottomNavBar
import edwinlovo.githubexplorer.presentation.ui.navigation.AppNavHost
import edwinlovo.githubexplorer.presentation.ui.navigation.BottomNavTab
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationRoute
import edwinlovo.githubexplorer.presentation.utils.ErrorEventBus
import edwinlovo.githubexplorer.presentation.utils.ObserveAsEvents
import edwinlovo.githubexplorer.presentation.utils.UiText

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    startDestination: NavigationRoute,
) {
    var errorToShow by remember { mutableStateOf<UiText?>(null) }
    ObserveAsEvents(ErrorEventBus.events) { errorToShow = it }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = BottomNavTab.entries.find { tab ->
        backStackEntry?.destination?.hierarchy?.any { it.hasRoute(tab.route::class) } == true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.weight(1f),
        )
        if (selectedTab != null) {
            GheBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.navigateToTab(it.route) },
            )
        }
    }

    HandleNavigation(viewModelNav = viewModel, navController = navController)

    errorToShow?.let { error ->
        GitHubExplorerErrorAlertDialog(
            message = error,
            onDismiss = { errorToShow = null },
        )
    }
}
