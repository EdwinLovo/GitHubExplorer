package edwinlovo.githubexplorer.presentation.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme

@Composable
fun GheDialogContainer(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(fraction = 0.62f),
            shape = RoundedCornerShape(GheTheme.cornerRadius.lg),
            color = GheTheme.colors.backgroundDefault,
            content = { content() },
        )
    }
}
