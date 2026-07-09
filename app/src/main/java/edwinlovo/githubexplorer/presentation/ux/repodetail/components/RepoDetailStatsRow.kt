package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.repodetail.utils.previewRepoDetails

@Composable
internal fun RepoDetailStatsRow(
    repo: RepoDetails,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
        horizontalArrangement = Arrangement.spacedBy(GheTheme.spacing.sm),
    ) {
        RepoDetailStatTile(
            iconRes = GheIcons.Star,
            label = stringResource(R.string.repodetail_stars),
            value = repo.stargazersCount,
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
        RepoDetailStatTile(
            iconRes = GheIcons.Fork,
            label = stringResource(R.string.repodetail_forks),
            value = repo.forksCount,
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
        RepoDetailStatTile(
            iconRes = GheIcons.Eye,
            label = stringResource(R.string.repodetail_watchers),
            value = repo.watchersCount,
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
        RepoDetailStatTile(
            iconRes = GheIcons.Issue,
            label = stringResource(R.string.repodetail_open_issues),
            value = repo.openIssuesCount,
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
    }
}

@GhePreview
@Composable
private fun RepoDetailStatsRowPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailStatsRow(repo = previewRepoDetails())
    }
}
