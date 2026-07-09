package edwinlovo.githubexplorer.presentation.ux.languagepicker

import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class LanguagePickerRoute(val language: String) : NavigationRoute
