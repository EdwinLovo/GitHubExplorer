package edwinlovo.githubexplorer.presentation.ux.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.domain.repository.SearchRepository
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.utils.DEBOUNCE_TIME
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING
import edwinlovo.githubexplorer.presentation.utils.ext.reduce
import edwinlovo.githubexplorer.presentation.ux.userprofile.UserProfileRoute
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersEvent
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersUiState
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<UsersUiState>
        field = MutableStateFlow(UsersUiState())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val users: Flow<PagingData<GithubUser>> = uiState
        .map { it.searchQuery }
        .distinctUntilChanged()
        .debounce { if (it.isBlank()) 0L else DEBOUNCE_TIME }
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(PagingData.empty()) else searchRepository.searchUsers(query)
        }
        .cachedIn(viewModelScope)

    fun handleEvent(event: UsersEvent) {
        when (event) {
            is UsersEvent.OnSearchQueryChanged -> setSearchQuery(event.query)
            is UsersEvent.OnClearSearchQuery -> clearSearchQuery()
            is UsersEvent.OnUserClicked -> navigateToUserProfile(event.user)
        }
    }

    private fun setSearchQuery(query: String) {
        uiState.reduce { copy(searchQuery = query) }
    }

    private fun clearSearchQuery() {
        uiState.reduce { copy(searchQuery = EMPTY_STRING) }
    }

    private fun navigateToUserProfile(user: GithubUser) {
        navigate(UserProfileRoute(username = user.login))
    }
}
