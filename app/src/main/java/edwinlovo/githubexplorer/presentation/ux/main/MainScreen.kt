package edwinlovo.githubexplorer.presentation.ux.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import edwinlovo.githubexplorer.presentation.ui.components.dialogs.GitHubExplorerErrorAlertDialog
import edwinlovo.githubexplorer.presentation.ui.navigation.AppNavHost
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

    AppNavHost(navController, startDestination)
    HandleNavigation(viewModelNav = viewModel, navController = navController)

    errorToShow?.let { error ->
        GitHubExplorerErrorAlertDialog(
            message = error,
            onDismiss = { errorToShow = null },
        )
    }
}
