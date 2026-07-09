package edwinlovo.githubexplorer.presentation.ux.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.repository.UserRepository
import edwinlovo.githubexplorer.domain.utils.onError
import edwinlovo.githubexplorer.domain.utils.onSuccess
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.utils.ext.reduce
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileEvent
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val username: String = checkNotNull(savedStateHandle[USERNAME_ARG])

    val uiState: StateFlow<UserProfileUiState>
        field = MutableStateFlow(UserProfileUiState())

    val repos: Flow<PagingData<GithubRepo>> =
        userRepository.getUserRepos(username).cachedIn(viewModelScope)

    init {
        loadUser()
    }

    fun handleEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.OnBackClicked -> popBackStack()
            is UserProfileEvent.OnRetryClicked -> loadUser()
            is UserProfileEvent.OnRepoClicked -> navigateToRepoDetail(event.repo)
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            uiState.reduce { copy(isLoading = true, hasError = false, isRateLimited = false) }
            userRepository.getUser(username)
                .onSuccess { user ->
                    uiState.reduce { copy(isLoading = false, user = user) }
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

    private fun navigateToRepoDetail(repo: GithubRepo) {
        navigate(RepoDetailRoute(owner = repo.ownerLogin, repo = repo.name))
    }

    companion object {
        private const val USERNAME_ARG = "username"
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_TOO_MANY_REQUESTS = 429
    }
}
