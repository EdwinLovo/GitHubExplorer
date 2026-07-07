package edwinlovo.githubexplorer.presentation.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.utils.UiText
import edwinlovo.githubexplorer.presentation.utils.asString

@Composable
fun GitHubExplorerErrorAlertDialog(
    message: UiText,
    onDismiss: () -> Unit,
) {
    GitHubExplorerAlertDialog(
        title = stringResource(R.string.error_alert_title),
        message = message.asString(),
        primaryButtonText = stringResource(R.string.error_alert_dismiss),
        onPrimaryClick = onDismiss,
        onDismiss = onDismiss,
    )
}
