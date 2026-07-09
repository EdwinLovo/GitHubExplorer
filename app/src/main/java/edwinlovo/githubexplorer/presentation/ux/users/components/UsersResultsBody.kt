package edwinlovo.githubexplorer.presentation.ux.users.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import edwinlovo.githubexplorer.presentation.ui.components.feedback.PagingLoadStateFooter
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.users.utils.previewUsers
import edwinlovo.githubexplorer.presentation.ux.users.utils.previewUsersPagingData
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun UsersResultsBody(
    users: LazyPagingItems<GithubUser>,
    onUserClick: (GithubUser) -> Unit,
) {
    val refreshState = users.loadState.refresh
    when {
        refreshState is LoadState.Loading && users.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        refreshState is LoadState.Error -> {
            UsersErrorState(
                isRateLimited = refreshState.error is RateLimitedException,
                onRetry = { users.retry() },
            )
        }
        refreshState is LoadState.NotLoading && users.itemCount == 0 -> {
            UsersEmptyState()
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = users.itemCount,
                    key = users.itemKey { it.id },
                ) { index ->
                    val user = users[index]
                    if (user != null) {
                        UserListItem(user = user, onClick = onUserClick)
                    }
                }
                item {
                    PagingLoadStateFooter(
                        loadState = users.loadState.append,
                        onRetry = { users.retry() },
                    )
                }
            }
        }
    }
}

@GhePreview
@Composable
private fun UsersResultsBodyPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        UsersResultsBody(
            users = flowOf(PagingData.from(previewUsers())).collectAsLazyPagingItems(),
            onUserClick = {},
        )
    }
}

@GhePreview
@Composable
private fun UsersResultsBodyLoadingPreview() {
    GitHubExplorerPreviewContainer {
        UsersResultsBody(
            users = flowOf(previewUsersPagingData(refresh = LoadState.Loading)).collectAsLazyPagingItems(),
            onUserClick = {},
        )
    }
}

@GhePreview
@Composable
private fun UsersResultsBodyErrorPreview() {
    GitHubExplorerPreviewContainer {
        UsersResultsBody(
            users = flowOf(previewUsersPagingData(refresh = LoadState.Error(RuntimeException("boom")))).collectAsLazyPagingItems(),
            onUserClick = {},
        )
    }
}

@GhePreview
@Composable
private fun UsersResultsBodyEmptyPreview() {
    GitHubExplorerPreviewContainer {
        UsersResultsBody(
            users = flowOf(previewUsersPagingData(refresh = LoadState.NotLoading(endOfPaginationReached = true))).collectAsLazyPagingItems(),
            onUserClick = {},
        )
    }
}
