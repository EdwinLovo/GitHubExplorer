package edwinlovo.githubexplorer.presentation.ux.userprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import edwinlovo.githubexplorer.presentation.ui.components.feedback.PagingLoadStateFooter
import edwinlovo.githubexplorer.presentation.ui.components.rows.RepoListItem
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileErrorState
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileHeader
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileMetadata
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileReposEmpty
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileReposSectionTitle
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileStatsRow
import edwinlovo.githubexplorer.presentation.ux.userprofile.components.UserProfileTopBar
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileEvent
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileUiState
import edwinlovo.githubexplorer.presentation.ux.userprofile.utils.previewUserProfile
import edwinlovo.githubexplorer.presentation.ux.userprofile.utils.previewUserRepos
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val repos = viewModel.repos.collectAsLazyPagingItems()

    UserProfileScreenContent(
        uiState = uiState,
        repos = repos,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileScreenContent(
    uiState: UserProfileUiState,
    repos: LazyPagingItems<GithubRepo>,
    handleEvent: (UserProfileEvent) -> Unit,
) {
    val user = uiState.user
    GheScaffold(
        topBar = {
            UserProfileTopBar(
                title = user?.login.orEmpty(),
                onBackClicked = { handleEvent(UserProfileEvent.OnBackClicked) },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.hasError || user == null -> {
                    UserProfileErrorState(
                        isRateLimited = uiState.isRateLimited,
                        onRetry = { handleEvent(UserProfileEvent.OnRetryClicked) },
                    )
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = {
                            handleEvent(UserProfileEvent.OnRetryClicked)
                            repos.refresh()
                        },
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                UserProfileHeader(user = user)
                                Spacer(modifier = Modifier.size(GheTheme.spacing.md))
                            }
                            item {
                                UserProfileStatsRow(user = user)
                                Spacer(modifier = Modifier.size(GheTheme.spacing.md))
                            }
                            item {
                                UserProfileMetadata(user = user)
                                Spacer(modifier = Modifier.size(GheTheme.spacing.md))
                            }
                            item { UserProfileReposSectionTitle() }
                            val refreshState = repos.loadState.refresh
                            when {
                                refreshState is LoadState.Loading && repos.itemCount == 0 -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(GheTheme.padding.lg),
                                            contentAlignment = Alignment.Center,
                                        ) { CircularProgressIndicator() }
                                    }
                                }
                                refreshState is LoadState.NotLoading && repos.itemCount == 0 -> {
                                    item { UserProfileReposEmpty() }
                                }
                                else -> {
                                    items(
                                        count = repos.itemCount,
                                        key = repos.itemKey { it.id },
                                    ) { index ->
                                        val repo = repos[index]
                                        if (repo != null) {
                                            RepoListItem(
                                                repo = repo,
                                                onClick = { handleEvent(UserProfileEvent.OnRepoClicked(it)) },
                                            )
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
                }
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun UserProfileScreenContentLoadedPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileScreenContent(
            uiState = UserProfileUiState(isLoading = false, user = previewUserProfile()),
            repos = flowOf(PagingData.from(previewUserRepos())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun UserProfileScreenContentLoadingPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileScreenContent(
            uiState = UserProfileUiState(isLoading = true),
            repos = flowOf(PagingData.from(emptyList<GithubRepo>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun UserProfileScreenContentErrorPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileScreenContent(
            uiState = UserProfileUiState(isLoading = false, hasError = true, isRateLimited = false),
            repos = flowOf(PagingData.from(emptyList<GithubRepo>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun UserProfileScreenContentRateLimitedPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileScreenContent(
            uiState = UserProfileUiState(isLoading = false, hasError = true, isRateLimited = true),
            repos = flowOf(PagingData.from(emptyList<GithubRepo>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun UserProfileScreenContentEmptyReposPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileScreenContent(
            uiState = UserProfileUiState(isLoading = false, user = previewUserProfile()),
            repos = flowOf(PagingData.from(emptyList<GithubRepo>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}
