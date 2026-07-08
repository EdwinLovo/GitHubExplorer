package edwinlovo.githubexplorer.presentation.ux.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreEmptyState
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreErrorState
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreSearchBar
import edwinlovo.githubexplorer.presentation.ux.explore.components.PagingLoadStateFooter
import edwinlovo.githubexplorer.presentation.ux.explore.components.RepoListItem
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreEvent
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreUiState
import edwinlovo.githubexplorer.presentation.ux.explore.utils.previewRepos
import kotlinx.coroutines.flow.flowOf

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val repos = viewModel.repos.collectAsLazyPagingItems()

    ExploreScreenContent(
        uiState = uiState,
        repos = repos,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreScreenContent(
    uiState: ExploreUiState,
    repos: LazyPagingItems<GithubRepo>,
    handleEvent: (ExploreEvent) -> Unit,
) {
    GheScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ExploreSearchBar(
                query = uiState.searchQuery,
                onQueryChanged = { handleEvent(ExploreEvent.OnSearchQueryChanged(it)) },
                onClearQuery = { handleEvent(ExploreEvent.OnClearSearchQuery) },
                modifier = Modifier.padding(
                    horizontal = GheTheme.padding.sm,
                    vertical = GheTheme.padding.xxs,
                ),
            )
            PullToRefreshBox(
                isRefreshing = repos.loadState.refresh is LoadState.Loading && repos.itemCount > 0,
                onRefresh = { repos.refresh() },
                modifier = Modifier.fillMaxSize(),
            ) {
                ExploreResultsBody(
                    query = uiState.searchQuery,
                    repos = repos,
                )
            }
        }
    }
}

@Composable
private fun ExploreResultsBody(
    query: String,
    repos: LazyPagingItems<GithubRepo>,
) {
    val refreshState = repos.loadState.refresh
    when {
        refreshState is LoadState.Loading && repos.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        refreshState is LoadState.Error -> {
            ExploreErrorState(
                isRateLimited = refreshState.error is RateLimitedException,
                onRetry = { repos.retry() },
            )
        }
        refreshState is LoadState.NotLoading && repos.itemCount == 0 -> {
            ExploreEmptyState(query = query)
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = repos.itemCount,
                    key = repos.itemKey { it.id },
                ) { index ->
                    val repo = repos[index]
                    if (repo != null) {
                        RepoListItem(repo = repo, onClick = {})
                    }
                }
                item {
                    PagingLoadStateFooter(
                        loadState = repos.loadState.append,
                        onRetry = { repos.retry() },
                    )
                }
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun ExploreScreenContentPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        ExploreScreenContent(
            uiState = ExploreUiState(searchQuery = ""),
            repos = flowOf(PagingData.from(previewRepos())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun ExploreScreenContentEmptyPreview() {
    GitHubExplorerPreviewContainer {
        ExploreScreenContent(
            uiState = ExploreUiState(searchQuery = "asdfgh"),
            repos = flowOf(PagingData.from(emptyList<GithubRepo>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}
