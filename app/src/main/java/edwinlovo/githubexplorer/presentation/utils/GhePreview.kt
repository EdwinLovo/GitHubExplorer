package edwinlovo.githubexplorer.presentation.utils

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, backgroundColor = 0xffffffff)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xff000000)
annotation class GhePreview

@Preview(name = "Light — Tablet", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, backgroundColor = 0xffffffff, widthDp = 1280, heightDp = 800)
@Preview(name = "Dark — Tablet", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xff000000, widthDp = 1280, heightDp = 800)
annotation class GhePreviewScreen

@Composable
fun GitHubExplorerPreviewContainer(content: @Composable () -> Unit) {
    GheTheme {
        Column(modifier = Modifier.padding(GheTheme.padding.xs)) {
            content()
        }
    }
}
