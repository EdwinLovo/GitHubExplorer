package edwinlovo.githubexplorer.presentation.ux.searchfilters

import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class SearchFiltersRoute(val language: String) : NavigationRoute
