package edwinlovo.githubexplorer.presentation.ux.explore.contracts

import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING

data class ExploreUiState(
    val searchQuery: String = EMPTY_STRING,
)

sealed interface ExploreEvent {
    data class OnSearchQueryChanged(val query: String) : ExploreEvent
    data object OnClearSearchQuery : ExploreEvent
}
