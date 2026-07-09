package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun RepoDetailErrorState(
    isRateLimited: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val messageRes = if (isRateLimited) R.string.error_rate_limited else R.string.generic_error
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(GheTheme.padding.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(messageRes),
            style = MaterialTheme.typography.bodyLarge,
            color = GheTheme.colors.textDefault,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = GheTheme.spacing.lg),
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@GhePreview
@Composable
private fun RepoDetailErrorStateGenericPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailErrorState(isRateLimited = false, onRetry = {})
    }
}

@GhePreview
@Composable
private fun RepoDetailErrorStateRateLimitedPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailErrorState(isRateLimited = true, onRetry = {})
    }
}
