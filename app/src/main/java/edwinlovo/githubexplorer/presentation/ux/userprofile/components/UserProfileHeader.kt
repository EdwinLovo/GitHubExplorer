package edwinlovo.githubexplorer.presentation.ux.userprofile.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.utils.MAX_LINES_SINGLE
import edwinlovo.githubexplorer.presentation.ux.userprofile.utils.previewUserProfile

@Composable
internal fun UserProfileHeader(
    user: UserProfile,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = stringResource(R.string.userprofile_avatar, user.login),
                modifier = Modifier
                    .size(GheTheme.iconSize.xxl)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(GheTheme.spacing.md))
            Column {
                Text(
                    text = user.name?.takeIf { it.isNotBlank() } ?: user.login,
                    style = MaterialTheme.typography.titleLarge,
                    color = GheTheme.colors.textDefault,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GheTheme.colors.textMuted,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        user.bio?.takeIf { it.isNotBlank() }?.let { bio ->
            Spacer(modifier = Modifier.size(GheTheme.spacing.md))
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyLarge,
                color = GheTheme.colors.textDefault,
            )
        }
    }
}

@GhePreview
@Composable
private fun UserProfileHeaderPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileHeader(user = previewUserProfile())
    }
}

@GhePreview
@Composable
private fun UserProfileHeaderNoBioPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileHeader(user = previewUserProfile().copy(bio = null, name = null))
    }
}
