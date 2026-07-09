package edwinlovo.githubexplorer.presentation.ux.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.repository.SearchRepository
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.utils.DEBOUNCE_TIME
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING
import edwinlovo.githubexplorer.presentation.utils.ext.reduce
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreEvent
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreUiState
import edwinlovo.githubexplorer.presentation.ux.repodetail.RepoDetailRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<ExploreUiState>
        field = MutableStateFlow(ExploreUiState())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val repos: Flow<PagingData<GithubRepo>> = uiState
        .map { it.searchQuery }
        .distinctUntilChanged()
        .debounce { if (it.isBlank()) 0L else DEBOUNCE_TIME }
        .flatMapLatest { query -> searchRepository.searchRepositories(query) }
        .cachedIn(viewModelScope)

    fun handleEvent(event: ExploreEvent) {
        when (event) {
            is ExploreEvent.OnSearchQueryChanged -> setSearchQuery(event.query)
            is ExploreEvent.OnClearSearchQuery -> clearSearchQuery()
            is ExploreEvent.OnRepoClicked -> navigateToRepoDetail(event.repo)
        }
    }

    private fun setSearchQuery(query: String) {
        uiState.reduce { copy(searchQuery = query) }
    }

    private fun clearSearchQuery() {
        uiState.reduce { copy(searchQuery = EMPTY_STRING) }
    }

    private fun navigateToRepoDetail(repo: GithubRepo) {
        navigate(RepoDetailRoute(owner = repo.ownerLogin, repo = repo.name))
    }
}
