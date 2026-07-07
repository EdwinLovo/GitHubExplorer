package edwinlovo.githubexplorer.presentation.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme

@Composable
fun GitHubExplorerAlertDialog(
    title: String,
    message: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onDismiss: () -> Unit,
    ghostButtonText: String? = null,
    onGhostClick: (() -> Unit)? = null,
) {
    GheDialogContainer(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(GheTheme.padding.xl),
            verticalArrangement = Arrangement.spacedBy(GheTheme.spacing.lg),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = GheTheme.colors.textDefault,
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = GheTheme.colors.textMuted,
            )

            Column(verticalArrangement = Arrangement.spacedBy(GheTheme.spacing.sm)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GheTheme.colors.backgroundPrimaryDefault),
                    onClick = onPrimaryClick,
                ) {
                    Text(text = primaryButtonText)
                }

                if (ghostButtonText != null) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onGhostClick ?: onDismiss,
                    ) {
                        Text(text = ghostButtonText, color = GheTheme.colors.textPrimary)
                    }
                }
            }
        }
    }
}
