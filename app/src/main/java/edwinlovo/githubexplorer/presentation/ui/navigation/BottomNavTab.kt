package edwinlovo.githubexplorer.presentation.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.ux.explore.ExploreRoute
import edwinlovo.githubexplorer.presentation.ux.favorites.FavoritesRoute
import edwinlovo.githubexplorer.presentation.ux.users.UsersRoute

enum class BottomNavTab(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val icon: Int,
    val route: NavigationRoute,
) {
    Explore(R.string.nav_tab_explore, GheIcons.Search, ExploreRoute),
    Users(R.string.nav_tab_users, GheIcons.Users, UsersRoute),
    Favorites(R.string.nav_tab_favorites, GheIcons.Favorite, FavoritesRoute),
}
