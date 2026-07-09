package edwinlovo.githubexplorer.presentation.ux.users.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun UsersSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(GheTheme.cornerRadius.lg),
        placeholder = { Text(text = stringResource(R.string.users_search_placeholder)) },
        leadingIcon = {
            Icon(
                painter = painterResource(GheIcons.Search),
                contentDescription = null,
                tint = GheTheme.colors.iconMuted,
                modifier = Modifier.size(GheTheme.iconSize.md),
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        painter = painterResource(GheIcons.Close),
                        contentDescription = stringResource(R.string.users_search_clear),
                        tint = GheTheme.colors.iconMuted,
                        modifier = Modifier.size(GheTheme.iconSize.md),
                    )
                }
            }
        },
    )
}

@GhePreview
@Composable
private fun UsersSearchBarEmptyPreview() {
    GitHubExplorerPreviewContainer {
        UsersSearchBar(query = EMPTY_STRING, onQueryChanged = {}, onClearQuery = {})
    }
}

@GhePreview
@Composable
private fun UsersSearchBarWithQueryPreview() {
    GitHubExplorerPreviewContainer {
        UsersSearchBar(query = "torvalds", onQueryChanged = {}, onClearQuery = {})
    }
}
