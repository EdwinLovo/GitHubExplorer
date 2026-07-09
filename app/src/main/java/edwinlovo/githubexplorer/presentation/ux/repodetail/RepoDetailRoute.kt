package edwinlovo.githubexplorer.presentation.ux.repodetail

import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailRoute(
    val owner: String,
    val repo: String,
) : NavigationRoute
