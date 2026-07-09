package edwinlovo.githubexplorer.presentation.ux.repodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.domain.model.favorites.toFavoriteRepo
import edwinlovo.githubexplorer.domain.repository.FavoritesRepository
import edwinlovo.githubexplorer.domain.repository.RepoRepository
import edwinlovo.githubexplorer.domain.utils.onError
import edwinlovo.githubexplorer.domain.utils.onSuccess
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.utils.ext.reduce
import edwinlovo.githubexplorer.presentation.ux.repodetail.contracts.RepoDetailEvent
import edwinlovo.githubexplorer.presentation.ux.repodetail.contracts.RepoDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repoRepository: RepoRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val owner: String = checkNotNull(savedStateHandle[OWNER_ARG])
    private val repo: String = checkNotNull(savedStateHandle[REPO_ARG])

    val uiState: StateFlow<RepoDetailUiState>
        field = MutableStateFlow(RepoDetailUiState())

    init {
        loadRepo()
        observeFavoriteState()
    }

    fun handleEvent(event: RepoDetailEvent) {
        when (event) {
            is RepoDetailEvent.OnBackClicked -> popBackStack()
            is RepoDetailEvent.OnRetryClicked -> loadRepo()
            is RepoDetailEvent.OnFavoriteToggled -> toggleFavorite()
            is RepoDetailEvent.OnOpenInBrowserClicked -> Unit
        }
    }

    private fun loadRepo() {
        viewModelScope.launch {
            uiState.reduce { copy(isLoading = true, hasError = false, isRateLimited = false) }
            repoRepository.getRepository(owner, repo)
                .onSuccess { details ->
                    uiState.reduce { copy(isLoading = false, repo = details) }
                }
                .onError { code, _ ->
                    uiState.reduce {
                        copy(
                            isLoading = false,
                            hasError = true,
                            isRateLimited = code == HTTP_FORBIDDEN || code == HTTP_TOO_MANY_REQUESTS,
                        )
                    }
                }
        }
    }

    private fun observeFavoriteState() {
        viewModelScope.launch {
            favoritesRepository.isFavorite(owner, repo).collect { isFavorite ->
                uiState.reduce { copy(isFavorite = isFavorite) }
            }
        }
    }

    private fun toggleFavorite() {
        val repo = uiState.value.repo ?: return
        val currentlyFavorite = uiState.value.isFavorite
        viewModelScope.launch {
            if (currentlyFavorite) {
                favoritesRepository.removeFavorite(repo.id)
            } else {
                favoritesRepository.addFavorite(repo.toFavoriteRepo())
            }
        }
    }

    companion object {
        private const val OWNER_ARG = "owner"
        private const val REPO_ARG = "repo"
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_TOO_MANY_REQUESTS = 429
    }
}
