package edwinlovo.githubexplorer.presentation.ux.users

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
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.users.components.UsersIdleState
import edwinlovo.githubexplorer.presentation.ux.users.components.UsersResultsBody
import edwinlovo.githubexplorer.presentation.ux.users.components.UsersSearchBar
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersEvent
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersUiState
import edwinlovo.githubexplorer.presentation.ux.users.utils.previewUsers
import kotlinx.coroutines.flow.flowOf

@Composable
fun UsersScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val users = viewModel.users.collectAsLazyPagingItems()

    UsersScreenContent(
        uiState = uiState,
        users = users,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersScreenContent(
    uiState: UsersUiState,
    users: LazyPagingItems<GithubUser>,
    handleEvent: (UsersEvent) -> Unit,
) {
    GheScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            UsersSearchBar(
                query = uiState.searchQuery,
                onQueryChanged = { handleEvent(UsersEvent.OnSearchQueryChanged(it)) },
                onClearQuery = { handleEvent(UsersEvent.OnClearSearchQuery) },
                modifier = Modifier.padding(
                    horizontal = GheTheme.padding.sm,
                    vertical = GheTheme.padding.xxs,
                ),
            )
            if (uiState.searchQuery.isBlank()) {
                UsersIdleState()
            } else {
                PullToRefreshBox(
                    isRefreshing = users.loadState.refresh is LoadState.Loading && users.itemCount > 0,
                    onRefresh = { users.refresh() },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    UsersResultsBody(
                        users = users,
                        onUserClick = { user -> handleEvent(UsersEvent.OnUserClicked(user)) },
                    )
                }
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun UsersScreenContentIdlePreview() {
    GitHubExplorerPreviewContainer {
        UsersScreenContent(
            uiState = UsersUiState(searchQuery = ""),
            users = flowOf(PagingData.from(emptyList<GithubUser>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun UsersScreenContentPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        UsersScreenContent(
            uiState = UsersUiState(searchQuery = "torvalds"),
            users = flowOf(PagingData.from(previewUsers())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun UsersScreenContentEmptyPreview() {
    GitHubExplorerPreviewContainer {
        UsersScreenContent(
            uiState = UsersUiState(searchQuery = "asdfgh"),
            users = flowOf(PagingData.from(emptyList<GithubUser>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}
