package edwinlovo.githubexplorer.presentation.ui.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edwinlovo.githubexplorer.presentation.ux.home.HomeRoute
import edwinlovo.githubexplorer.presentation.ux.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: NavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.systemBarsPadding(),
    ) {
        // Home
        composable<HomeRoute> { HomeScreen(navController) }
    }
}
