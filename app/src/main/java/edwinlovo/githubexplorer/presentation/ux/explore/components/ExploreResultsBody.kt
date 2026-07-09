package edwinlovo.githubexplorer.presentation.ux.explore.components

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
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.explore.utils.previewPagingData
import edwinlovo.githubexplorer.presentation.ux.explore.utils.previewRepos
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun ExploreResultsBody(
    query: String,
    repos: LazyPagingItems<GithubRepo>,
    onRepoClick: (GithubRepo) -> Unit,
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
                        RepoListItem(repo = repo, onClick = onRepoClick)
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

@GhePreview
@Composable
private fun ExploreResultsBodyPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        ExploreResultsBody(
            query = EMPTY_STRING,
            repos = flowOf(PagingData.from(previewRepos())).collectAsLazyPagingItems(),
            onRepoClick = {},
        )
    }
}

@GhePreview
@Composable
private fun ExploreResultsBodyLoadingPreview() {
    GitHubExplorerPreviewContainer {
        ExploreResultsBody(
            query = EMPTY_STRING,
            repos = flowOf(previewPagingData(refresh = LoadState.Loading)).collectAsLazyPagingItems(),
            onRepoClick = {},
        )
    }
}

@GhePreview
@Composable
private fun ExploreResultsBodyErrorPreview() {
    GitHubExplorerPreviewContainer {
        ExploreResultsBody(
            query = "compose",
            repos = flowOf(previewPagingData(refresh = LoadState.Error(RuntimeException("boom")))).collectAsLazyPagingItems(),
            onRepoClick = {},
        )
    }
}

@GhePreview
@Composable
private fun ExploreResultsBodyEmptyPreview() {
    GitHubExplorerPreviewContainer {
        ExploreResultsBody(
            query = "asdfgh",
            repos = flowOf(previewPagingData(refresh = LoadState.NotLoading(endOfPaginationReached = true))).collectAsLazyPagingItems(),
            onRepoClick = {},
        )
    }
}

