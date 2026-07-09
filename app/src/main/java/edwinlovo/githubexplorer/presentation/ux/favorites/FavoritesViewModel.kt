package edwinlovo.githubexplorer.presentation.ux.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import edwinlovo.githubexplorer.domain.repository.FavoritesRepository
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.utils.ext.reduce
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesEvent
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesUiState
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<FavoritesUiState>
        field = MutableStateFlow(FavoritesUiState())

    init {
        observeFavorites()
    }

    fun handleEvent(event: FavoritesEvent) {
        when (event) {
            is FavoritesEvent.OnRepoClicked -> navigateToRepoDetail(event.favorite)
            is FavoritesEvent.OnFavoriteDismissed -> removeFavorite(event.favorite)
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesRepository.observeFavorites().collect { favorites ->
                uiState.reduce { copy(isLoading = false, favorites = favorites) }
            }
        }
    }

    private fun navigateToRepoDetail(favorite: FavoriteRepo) {
        navigate(RepoDetailRoute(owner = favorite.ownerLogin, repo = favorite.name))
    }

    private fun removeFavorite(favorite: FavoriteRepo) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(favorite.id)
        }
    }
}
