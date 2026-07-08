package edwinlovo.githubexplorer.presentation.ux.explore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun PagingLoadStateFooter(
    loadState: LoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(GheTheme.padding.sm),
        contentAlignment = Alignment.Center,
    ) {
        when (loadState) {
            is LoadState.Loading -> CircularProgressIndicator(
                modifier = Modifier.size(GheTheme.iconSize.md),
            )
            is LoadState.Error -> {
                val isRateLimited = loadState.error is RateLimitedException
                val messageRes = if (isRateLimited) R.string.error_rate_limited else R.string.generic_error
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(messageRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = GheTheme.colors.textMuted,
                        textAlign = TextAlign.Center,
                    )
                    TextButton(onClick = onRetry) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }
            is LoadState.NotLoading -> Unit
        }
    }
}

@GhePreview
@Composable
private fun PagingLoadStateFooterLoadingPreview() {
    GitHubExplorerPreviewContainer {
        PagingLoadStateFooter(loadState = LoadState.Loading, onRetry = {})
    }
}

@GhePreview
@Composable
private fun PagingLoadStateFooterErrorPreview() {
    GitHubExplorerPreviewContainer {
        PagingLoadStateFooter(
            loadState = LoadState.Error(RuntimeException("boom")),
            onRetry = {},
        )
    }
}

@GhePreview
@Composable
private fun PagingLoadStateFooterRateLimitedPreview() {
    GitHubExplorerPreviewContainer {
        PagingLoadStateFooter(
            loadState = LoadState.Error(RateLimitedException(403)),
            onRetry = {},
        )
    }
}
