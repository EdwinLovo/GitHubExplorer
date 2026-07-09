package edwinlovo.githubexplorer.presentation.ux.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import edwinlovo.githubexplorer.domain.model.response.search.toGithubRepo
import edwinlovo.githubexplorer.presentation.ui.components.rows.RepoListItem
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.favorites.utils.previewFavorites

@Composable
internal fun FavoriteSwipeItem(
    favorite: FavoriteRepo,
    onClick: (FavoriteRepo) -> Unit,
    onDismissed: (FavoriteRepo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDismissed(favorite)
                true
            } else {
                false
            }
        },
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        modifier = modifier,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GheTheme.colors.backgroundDestructiveDefault)
                    .padding(horizontal = GheTheme.padding.md),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    painter = painterResource(GheIcons.Delete),
                    contentDescription = stringResource(R.string.favorites_remove),
                    tint = GheTheme.colors.iconOnDestructive,
                    modifier = Modifier.size(GheTheme.iconSize.md),
                )
            }
        },
    ) {
        RepoListItem(
            repo = favorite.toGithubRepo(),
            onClick = { onClick(favorite) },
            modifier = Modifier.background(GheTheme.colors.backgroundDefault),
        )
    }
}

@GhePreview
@Composable
private fun FavoriteSwipeItemPreview() {
    GitHubExplorerPreviewContainer {
        FavoriteSwipeItem(
            favorite = previewFavorites().first(),
            onClick = {},
            onDismissed = {},
        )
    }
}
