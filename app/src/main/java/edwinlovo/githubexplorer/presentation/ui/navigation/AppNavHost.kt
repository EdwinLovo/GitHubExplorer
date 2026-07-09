package edwinlovo.githubexplorer.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edwinlovo.githubexplorer.presentation.ux.explore.ExploreRoute
import edwinlovo.githubexplorer.presentation.ux.explore.ExploreScreen
import edwinlovo.githubexplorer.presentation.ux.favorites.FavoritesRoute
import edwinlovo.githubexplorer.presentation.ux.favorites.FavoritesScreen
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailScreen
import edwinlovo.githubexplorer.presentation.ux.users.UsersRoute
import edwinlovo.githubexplorer.presentation.ux.users.UsersScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: NavigationRoute,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<ExploreRoute> { ExploreScreen(navController) }
        composable<FavoritesRoute> { FavoritesScreen(navController) }
        composable<RepoDetailRoute> { RepoDetailScreen(navController) }
        composable<UsersRoute> { UsersScreen(navController) }
    }
}
