package edwinlovo.githubexplorer.presentation.ux.repodetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun RepoTopicChip(
    topic: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = topic,
        style = MaterialTheme.typography.labelMedium,
        color = GheTheme.colors.textPrimary,
        modifier = modifier
            .clip(RoundedCornerShape(GheTheme.cornerRadius.full))
            .background(GheTheme.colors.backgroundPrimaryLight)
            .padding(horizontal = GheTheme.padding.xs, vertical = GheTheme.padding.xxs),
    )
}

@GhePreview
@Composable
private fun RepoTopicChipPreview() {
    GitHubExplorerPreviewContainer {
        RepoTopicChip(topic = "compose")
    }
}
