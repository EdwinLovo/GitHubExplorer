package edwinlovo.githubexplorer.presentation.ux.userprofile.contracts

import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile

data class UserProfileUiState(
    val isLoading: Boolean = true,
    val user: UserProfile? = null,
    val hasError: Boolean = false,
    val isRateLimited: Boolean = false,
)

sealed interface UserProfileEvent {
    data object OnBackClicked : UserProfileEvent
    data object OnRetryClicked : UserProfileEvent
    data class OnRepoClicked(val repo: GithubRepo) : UserProfileEvent
}
