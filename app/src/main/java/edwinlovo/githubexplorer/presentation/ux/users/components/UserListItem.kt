package edwinlovo.githubexplorer.presentation.ux.users.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.utils.MAX_LINES_SINGLE
import edwinlovo.githubexplorer.presentation.ux.users.utils.labelRes
import edwinlovo.githubexplorer.presentation.ux.users.utils.previewUsers

@Composable
internal fun UserListItem(
    user: GithubUser,
    onClick: (GithubUser) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(user) }
                .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = stringResource(R.string.users_user_avatar, user.login),
                modifier = Modifier
                    .size(GheTheme.iconSize.xl)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(GheTheme.spacing.md))
            Column(modifier = Modifier.weight(ABSOLUTE_WEIGHT)) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.titleMedium,
                    color = GheTheme.colors.textDefault,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(user.type.labelRes()),
                    style = MaterialTheme.typography.bodySmall,
                    color = GheTheme.colors.textMuted,
                    maxLines = MAX_LINES_SINGLE,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        HorizontalDivider(color = GheTheme.colors.borderMuted)
    }
}

@GhePreview
@Composable
private fun UserListItemUserPreview() {
    GitHubExplorerPreviewContainer {
        UserListItem(user = previewUsers().first(), onClick = {})
    }
}

@GhePreview
@Composable
private fun UserListItemOrganizationPreview() {
    GitHubExplorerPreviewContainer {
        UserListItem(user = previewUsers()[1], onClick = {})
    }
}
