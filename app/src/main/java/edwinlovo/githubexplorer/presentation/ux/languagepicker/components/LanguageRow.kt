package edwinlovo.githubexplorer.presentation.ux.languagepicker.components

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
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.SearchLanguage

@Composable
internal fun LanguageRow(
    language: SearchLanguage,
    isSelected: Boolean,
    onClick: (SearchLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(language) }
                .padding(horizontal = GheTheme.padding.sm, vertical = GheTheme.padding.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(language.labelRes),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) GheTheme.colors.textPrimary else GheTheme.colors.textDefault,
            )
        }
        HorizontalDivider(color = GheTheme.colors.borderMuted)
    }
}

@GhePreview
@Composable
private fun LanguageRowPreview() {
    GitHubExplorerPreviewContainer {
        LanguageRow(language = SearchLanguage.Kotlin, isSelected = false, onClick = {})
    }
}

@GhePreview
@Composable
private fun LanguageRowSelectedPreview() {
    GitHubExplorerPreviewContainer {
        LanguageRow(language = SearchLanguage.Kotlin, isSelected = true, onClick = {})
    }
}
