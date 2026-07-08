package edwinlovo.githubexplorer.presentation.ux.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreResultsBody
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreSearchBar
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
