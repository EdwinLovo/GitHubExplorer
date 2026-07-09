package edwinlovo.githubexplorer.presentation.ux.repodetail.contracts

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails

data class RepoDetailUiState(
    val isLoading: Boolean = true,
    val repo: RepoDetails? = null,
    val isFavorite: Boolean = false,
    val hasError: Boolean = false,
    val isRateLimited: Boolean = false,
)

sealed interface RepoDetailEvent {
    data object OnBackClicked : RepoDetailEvent
    data object OnRetryClicked : RepoDetailEvent
    data object OnFavoriteToggled : RepoDetailEvent
    data class OnOpenInBrowserClicked(val url: String) : RepoDetailEvent
}
