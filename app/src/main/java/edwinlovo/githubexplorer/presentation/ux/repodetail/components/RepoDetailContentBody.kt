package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.repodetail.utils.previewRepoDetails

@Composable
internal fun RepoDetailContentBody(
    repo: RepoDetails,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        RepoDetailHeader(repo = repo)
        Spacer(modifier = Modifier.size(GheTheme.spacing.md))
        RepoDetailStatsRow(repo = repo)
        Spacer(modifier = Modifier.size(GheTheme.spacing.md))
        RepoDetailMetadata(repo = repo)
        Spacer(modifier = Modifier.size(GheTheme.spacing.lg))
    }
}

@GhePreview
@Composable
private fun RepoDetailContentBodyPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailContentBody(repo = previewRepoDetails())
    }
}
