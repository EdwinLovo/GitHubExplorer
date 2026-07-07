---
name: component-primitives
description: First-time setup for the shared UI component stubs — the single GheScaffold, the GheDialogContainer container, and the GitHubExplorerAlertDialog / GitHubExplorerErrorAlertDialog variants. Invoke when scaffolding these in a new project, or when a rule references any of them and the file doesn't exist yet.
allowed-tools:
  - Read
  - Write
  - Edit
  - Grep
  - Glob
---

# Component primitives

The shared UI components every screen and dialog composes through. Copy each into the paths below. Placeholder substitution applies throughout.

For the conventions that use these components:
- `GheScaffold` → `rules/screens.md` (single-shared-scaffold rule)
- `GheDialogContainer`, `GitHubExplorerAlertDialog` → `rules/dialogs.md`
- `GitHubExplorerErrorAlertDialog` → `rules/error-handling.md` (rendered by `MainScreen` when the `ErrorEventBus` emits)

These depend on primitives from other skills: `GheTheme` tokens (`theme-primitives`) and `UiText` / `asString()` (`misc-primitives`). Run those first in a fresh project.

---

## File 1 — `presentation/ui/components/scaffold/GheScaffold.kt`

The single shared scaffold. Every feature screen renders through this — no per-feature `<Feature>Scaffold` variants. See `rules/screens.md`.

Screens pass their own top/bottom bars as slot arguments. Adjust the `containerColor` once `ExtendedColors` is populated (typically `backgroundMuted` or `backgroundDefault`).

```kotlin
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
```

---

## File 2 — `presentation/ui/components/dialogs/GheDialogContainer.kt`

The shared dialog wrapper. Every dialog composes through this — provides adaptive sizing, theme background, corner radius. See `rules/dialogs.md`.

The width fraction is a starting point (~62% at expanded window widths). Add a `WindowSizeClass` branch if your app supports both compact and expanded windows.

```kotlin
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
```

---

## File 3 — `presentation/ui/components/dialogs/GitHubExplorerAlertDialog.kt`

The standard confirmation dialog — a title, a message, a primary button, and an optional ghost button, stacked at the compact width. Confirmation and error dialogs use this instead of building a custom body. See `rules/dialogs.md`.

The `Button` / `TextButton` pair is a starting point — swap in your project's styled button component once one exists.

```kotlin
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
```

---

## File 4 — `presentation/ui/components/dialogs/GitHubExplorerErrorAlertDialog.kt`

The error variant rendered by `MainScreen` when the `ErrorEventBus` emits — takes a `UiText` directly. See `rules/error-handling.md`.

Requires two entries in `strings.xml`: `<string name="error_alert_title">Something went wrong</string>` and `<string name="error_alert_dismiss">OK</string>`.

```kotlin
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
```

---

## Verification after copying

Once these files exist:

- `presentation/ui/components/scaffold/GheScaffold.kt`
- `presentation/ui/components/dialogs/GheDialogContainer.kt`
- `presentation/ui/components/dialogs/GitHubExplorerAlertDialog.kt`
- `presentation/ui/components/dialogs/GitHubExplorerErrorAlertDialog.kt`

Every rule that references `GheScaffold { }`, `GheDialogContainer`, `GitHubExplorerAlertDialog`, or `GitHubExplorerErrorAlertDialog` will now resolve. See the corresponding rules linked at the top of this skill for usage patterns.
