package edwinlovo.githubexplorer.presentation.ux.userprofile.contracts

data class UserProfileUiState(
    val isLoading: Boolean = false,
)

sealed interface UserProfileEvent
