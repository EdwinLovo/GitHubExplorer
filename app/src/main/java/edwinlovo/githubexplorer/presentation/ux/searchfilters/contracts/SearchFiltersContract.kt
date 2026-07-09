package edwinlovo.githubexplorer.presentation.ux.searchfilters.contracts

import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING

data class SearchFiltersUiState(
    val language: String = EMPTY_STRING,
)

sealed interface SearchFiltersEvent {
    data object OnBackClicked : SearchFiltersEvent
    data object OnLanguageRowClicked : SearchFiltersEvent
}
