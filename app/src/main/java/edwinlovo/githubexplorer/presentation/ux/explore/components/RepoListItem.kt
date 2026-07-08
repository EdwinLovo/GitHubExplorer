package edwinlovo.githubexplorer.presentation.ux.explore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.utils.MAX_LINES_SINGLE
import edwinlovo.githubexplorer.presentation.ux.explore.utils.previewRepos

private const val DESCRIPTION_MAX_LINES = 2

@Composable
internal fun RepoListItem(
    repo: GithubRepo,
    onClick: (GithubRepo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(repo) }
                .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = stringResource(R.string.explore_repo_avatar, repo.ownerLogin),
                modifier = Modifier
                    .size(GheTheme.iconSize.xl)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(GheTheme.spacing.md))
            Column(modifier = Modifier.weight(ABSOLUTE_WEIGHT)) {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = GheTheme.colors.textDefault,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = repo.ownerLogin,
                    style = MaterialTheme.typography.bodySmall,
                    color = GheTheme.colors.textMuted,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
                repo.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Spacer(modifier = Modifier.size(GheTheme.spacing.xs))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GheTheme.colors.textDefault,
                        maxLines = DESCRIPTION_MAX_LINES,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.size(GheTheme.spacing.sm))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(GheTheme.spacing.md),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(GheIcons.Star),
                            contentDescription = stringResource(R.string.explore_repo_stars),
                            tint = GheTheme.colors.iconMuted,
                            modifier = Modifier.size(GheTheme.iconSize.xs),
                        )
                        Spacer(modifier = Modifier.width(GheTheme.spacing.xs))
                        Text(
                            text = repo.stargazersCount.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = GheTheme.colors.textMuted,
                        )
                    }
                    repo.language?.let { language ->
                        Text(
                            text = language,
                            style = MaterialTheme.typography.bodySmall,
                            color = GheTheme.colors.textMuted,
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = GheTheme.colors.borderMuted)
    }
}

@GhePreview
@Composable
private fun RepoListItemPreview() {
    GitHubExplorerPreviewContainer {
        RepoListItem(repo = previewRepos().first(), onClick = {})
    }
}
