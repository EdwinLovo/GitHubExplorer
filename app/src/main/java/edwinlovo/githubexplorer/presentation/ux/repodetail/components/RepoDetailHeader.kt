package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.utils.MAX_LINES_SINGLE
import edwinlovo.githubexplorer.presentation.ux.repodetail.utils.previewRepoDetails

@Composable
internal fun RepoDetailHeader(
    repo: RepoDetails,
    onOwnerClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOwnerClicked() }
                .padding(vertical = GheTheme.padding.xxs),
        ) {
            val avatarPlaceholder = ColorPainter(GheTheme.colors.backgroundMuted)
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = stringResource(R.string.repodetail_owner_avatar, repo.ownerLogin),
                placeholder = avatarPlaceholder,
                error = avatarPlaceholder,
                modifier = Modifier
                    .size(GheTheme.iconSize.xxl)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(GheTheme.spacing.md))
            Column {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = GheTheme.colors.textDefault,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = repo.ownerLogin,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GheTheme.colors.textMuted,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(modifier = Modifier.size(GheTheme.spacing.md))
        val description = repo.description?.takeIf { it.isNotBlank() }
            ?: stringResource(R.string.repodetail_no_description)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = if (repo.description.isNullOrBlank()) GheTheme.colors.textMuted else GheTheme.colors.textDefault,
        )
    }
}

@GhePreview
@Composable
private fun RepoDetailHeaderPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailHeader(repo = previewRepoDetails(), onOwnerClicked = {})
    }
}

@GhePreview
@Composable
private fun RepoDetailHeaderNoDescriptionPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailHeader(repo = previewRepoDetails().copy(description = null), onOwnerClicked = {})
    }
}
