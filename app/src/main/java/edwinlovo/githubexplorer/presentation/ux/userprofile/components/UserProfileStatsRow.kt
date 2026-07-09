package edwinlovo.githubexplorer.presentation.ux.userprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.userprofile.utils.previewUserProfile

@Composable
internal fun UserProfileStatsRow(
    user: UserProfile,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
        horizontalArrangement = Arrangement.spacedBy(GheTheme.spacing.sm),
    ) {
        UserProfileStatTile(
            value = user.followers,
            label = stringResource(R.string.userprofile_followers),
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
        UserProfileStatTile(
            value = user.following,
            label = stringResource(R.string.userprofile_following),
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
        UserProfileStatTile(
            value = user.publicRepos,
            label = stringResource(R.string.userprofile_public_repos),
            modifier = Modifier.weight(ABSOLUTE_WEIGHT),
        )
    }
}

@GhePreview
@Composable
private fun UserProfileStatsRowPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileStatsRow(user = previewUserProfile())
    }
}
