package edwinlovo.githubexplorer.presentation.ux.userprofile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun UserProfileStatTile(
    value: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(GheTheme.padding.xxs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = GheTheme.colors.textDefault,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = GheTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )
    }
}

@GhePreview
@Composable
private fun UserProfileStatTilePreview() {
    GitHubExplorerPreviewContainer {
        UserProfileStatTile(value = 1_234, label = "Followers")
    }
}
