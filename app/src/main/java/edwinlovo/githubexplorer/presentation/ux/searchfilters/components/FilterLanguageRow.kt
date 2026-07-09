package edwinlovo.githubexplorer.presentation.ux.searchfilters.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.toSearchLanguage

@Composable
internal fun FilterLanguageRow(
    language: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.filters_language_label),
                style = MaterialTheme.typography.bodyLarge,
                color = GheTheme.colors.textDefault,
                modifier = Modifier.weight(ABSOLUTE_WEIGHT),
            )
            Text(
                text = stringResource(language.toSearchLanguage().labelRes),
                style = MaterialTheme.typography.bodyLarge,
                color = GheTheme.colors.textPrimary,
            )
        }
        HorizontalDivider(color = GheTheme.colors.borderMuted)
    }
}

@GhePreview
@Composable
private fun FilterLanguageRowAnyPreview() {
    GitHubExplorerPreviewContainer {
        FilterLanguageRow(language = "", onClick = {})
    }
}

@GhePreview
@Composable
private fun FilterLanguageRowSelectedPreview() {
    GitHubExplorerPreviewContainer {
        FilterLanguageRow(language = "kotlin", onClick = {})
    }
}
