package edwinlovo.githubexplorer.presentation.ux.home.contracts

data class HomeUiState(
    val isLoading: Boolean = false,
)

sealed interface HomeEvent {
    data object OnLoad : HomeEvent
}
