package edwinlovo.githubexplorer.presentation.ux.userprofile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.utils.MAX_LINES_SINGLE

@Composable
internal fun UserProfileTopBar(
    title: String,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GheTheme.padding.xxs, vertical = GheTheme.padding.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                painter = painterResource(GheIcons.ArrowBack),
                contentDescription = stringResource(R.string.userprofile_back),
                tint = GheTheme.colors.iconDefault,
                modifier = Modifier.size(GheTheme.iconSize.md),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = GheTheme.colors.textDefault,
            maxLines = MAX_LINES_SINGLE,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(ABSOLUTE_WEIGHT)
                .padding(horizontal = GheTheme.spacing.sm),
        )
    }
}

@GhePreview
@Composable
private fun UserProfileTopBarPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileTopBar(title = "torvalds", onBackClicked = {})
    }
}
