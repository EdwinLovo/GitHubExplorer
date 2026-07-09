package edwinlovo.githubexplorer.presentation.ux.userprofile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
internal fun UserProfileReposEmpty(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(GheTheme.padding.lg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.userprofile_repos_empty),
            style = MaterialTheme.typography.bodyMedium,
            color = GheTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )
    }
}

@GhePreview
@Composable
private fun UserProfileReposEmptyPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileReposEmpty()
    }
}
