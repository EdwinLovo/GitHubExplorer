package edwinlovo.githubexplorer.presentation.ux.favorites.contracts

data class FavoritesUiState(
    val isLoading: Boolean = false,
)

sealed interface FavoritesEvent
