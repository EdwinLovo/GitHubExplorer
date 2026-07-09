package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun RepoDetailStatTile(
    @DrawableRes iconRes: Int,
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(GheTheme.padding.xxs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = GheTheme.colors.iconMuted,
            modifier = Modifier.size(GheTheme.iconSize.sm),
        )
        Spacer(modifier = Modifier.size(GheTheme.spacing.xs))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = GheTheme.colors.textDefault,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = GheTheme.colors.textMuted,
        )
    }
}

@GhePreview
@Composable
private fun RepoDetailStatTilePreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailStatTile(iconRes = GheIcons.Star, label = "Stars", value = 1_234)
    }
}
