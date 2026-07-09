package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.utils.ext.toFormattedDate
import edwinlovo.githubexplorer.presentation.ux.repodetail.utils.previewRepoDetails

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RepoDetailMetadata(
    repo: RepoDetails,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
    ) {
        repo.language?.let { language ->
            Text(
                text = language,
                style = MaterialTheme.typography.bodyMedium,
                color = GheTheme.colors.textDefault,
            )
            Spacer(modifier = Modifier.size(GheTheme.spacing.sm))
        }
        if (repo.topics.isNotEmpty()) {
            Text(
                text = stringResource(R.string.repodetail_topics),
                style = MaterialTheme.typography.labelLarge,
                color = GheTheme.colors.textMuted,
            )
            Spacer(modifier = Modifier.size(GheTheme.spacing.xs))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(GheTheme.spacing.xs),
                verticalArrangement = Arrangement.spacedBy(GheTheme.spacing.xs),
                modifier = Modifier.fillMaxWidth(),
            ) {
                repo.topics.forEach { topic -> RepoTopicChip(topic = topic) }
            }
            Spacer(modifier = Modifier.size(GheTheme.spacing.md))
        }
        repo.licenseName?.let { license ->
            Text(
                text = stringResource(R.string.repodetail_license, license),
                style = MaterialTheme.typography.bodyMedium,
                color = GheTheme.colors.textMuted,
            )
            Spacer(modifier = Modifier.size(GheTheme.spacing.xs))
        }
        val formattedDate = repo.updatedAt.toFormattedDate()
        if (formattedDate.isNotBlank()) {
            Text(
                text = stringResource(R.string.repodetail_last_updated, formattedDate),
                style = MaterialTheme.typography.bodyMedium,
                color = GheTheme.colors.textMuted,
            )
        }
    }
}

@GhePreview
@Composable
private fun RepoDetailMetadataPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailMetadata(repo = previewRepoDetails())
    }
}

@GhePreview
@Composable
private fun RepoDetailMetadataMinimalPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailMetadata(
            repo = previewRepoDetails().copy(topics = emptyList(), licenseName = null, language = null)
        )
    }
}
