package edwinlovo.githubexplorer.presentation.ux.userprofile

import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileRoute(val username: String) : NavigationRoute
