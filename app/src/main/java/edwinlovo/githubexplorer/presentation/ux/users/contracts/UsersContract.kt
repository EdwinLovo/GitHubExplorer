package edwinlovo.githubexplorer.presentation.ux.users.contracts

data class UsersUiState(
    val isLoading: Boolean = false,
)

sealed interface UsersEvent
