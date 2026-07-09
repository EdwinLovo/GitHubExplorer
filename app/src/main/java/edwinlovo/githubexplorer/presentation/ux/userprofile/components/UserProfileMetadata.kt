package edwinlovo.githubexplorer.presentation.ux.userprofile.components

import androidx.compose.foundation.layout.Column
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
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.userprofile.utils.previewUserProfile

@Composable
internal fun UserProfileMetadata(
    user: UserProfile,
    modifier: Modifier = Modifier,
) {
    val rows = listOfNotNull(
        user.company?.takeIf { it.isNotBlank() }?.let { stringResource(R.string.userprofile_company, it) },
        user.location?.takeIf { it.isNotBlank() }?.let { stringResource(R.string.userprofile_location, it) },
        user.blog?.takeIf { it.isNotBlank() }?.let { stringResource(R.string.userprofile_blog, it) },
    )
    if (rows.isEmpty()) return
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.xxs),
    ) {
        rows.forEachIndexed { index, text ->
            if (index > 0) Spacer(modifier = Modifier.size(GheTheme.spacing.xs))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = GheTheme.colors.textMuted,
            )
        }
    }
}

@GhePreview
@Composable
private fun UserProfileMetadataPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileMetadata(user = previewUserProfile())
    }
}

@GhePreview
@Composable
private fun UserProfileMetadataMinimalPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileMetadata(
            user = previewUserProfile().copy(company = null, location = null, blog = null),
        )
    }
}
