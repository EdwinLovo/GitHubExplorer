package edwinlovo.githubexplorer.presentation.ux.favorites.contracts

import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<FavoriteRepo> = emptyList(),
)

sealed interface FavoritesEvent {
    data class OnRepoClicked(val favorite: FavoriteRepo) : FavoritesEvent
    data class OnFavoriteDismissed(val favorite: FavoriteRepo) : FavoritesEvent
}
