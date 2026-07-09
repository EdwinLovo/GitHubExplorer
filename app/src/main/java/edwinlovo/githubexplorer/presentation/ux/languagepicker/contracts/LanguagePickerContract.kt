package edwinlovo.githubexplorer.presentation.ux.languagepicker.contracts

import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.SearchLanguage

data class LanguagePickerUiState(
    val selected: String = EMPTY_STRING,
)

sealed interface LanguagePickerEvent {
    data object OnBackClicked : LanguagePickerEvent
    data class OnLanguageSelected(val language: SearchLanguage) : LanguagePickerEvent
}
