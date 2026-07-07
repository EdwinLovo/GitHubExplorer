package edwinlovo.githubexplorer.presentation.ui.components.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme

@Composable
fun GheScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = GheTheme.colors.backgroundDefault,
        topBar = topBar ?: {},
        bottomBar = bottomBar ?: {},
        content = content,
    )
}
