package edwinlovo.githubexplorer.presentation.ux.userprofile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun UserProfileReposSectionTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.userprofile_repos_title),
        style = MaterialTheme.typography.titleMedium,
        color = GheTheme.colors.textDefault,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xs),
    )
}

@GhePreview
@Composable
private fun UserProfileReposSectionTitlePreview() {
    GitHubExplorerPreviewContainer {
        UserProfileReposSectionTitle()
    }
}
