package edwinlovo.githubexplorer.presentation.ux.explore.contracts

import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING

data class ExploreUiState(
    val searchQuery: String = EMPTY_STRING,
    val languageFilter: String = EMPTY_STRING,
    val showFilterSnackbar: Boolean = false,
)

sealed interface ExploreEvent {
    data class OnSearchQueryChanged(val query: String) : ExploreEvent
    data object OnClearSearchQuery : ExploreEvent
    data class OnRepoClicked(val repo: GithubRepo) : ExploreEvent
    data object OnFilterClicked : ExploreEvent
    data class OnLanguageFilterResult(val language: String) : ExploreEvent
    data object OnFilterSnackbarShown : ExploreEvent
}
