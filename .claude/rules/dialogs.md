---
name: dialogs
description: Every dialog uses GheDialogContainer as outermost wrapper; use AlertDialog variant for confirmations
paths:
  - "**/dialogs/**/*.kt"
  - "**/*Dialog*.kt"
---

# Dialogs

Every dialog in the app is wrapped in `GheDialogContainer` (`presentation/ui/components/dialogs/GheDialogContainer.kt`) as its outermost composable. It provides consistent adaptive sizing, shape, background color, and elevation.

**Never** re-declare `Dialog { }`, `Surface { }`, or size logic directly inside a dialog composable. That's exactly what `GheDialogContainer` is for.

**Setting up the dialog components in a new project:** invoke the `component-primitives` skill (`.claude/skills/component-primitives/SKILL.md`). It ships `GheDialogContainer.kt`, `GitHubExplorerAlertDialog.kt`, and `GitHubExplorerErrorAlertDialog.kt` under `presentation/ui/components/dialogs/`.

## Custom dialog

```kotlin
@Composable
internal fun ConfirmDeleteDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    GheDialogContainer(onDismissRequest = onDismiss) {
        Column {
            Text(text = stringResource(R.string.confirm_delete_title))
            Text(text = stringResource(R.string.confirm_delete_message, itemName))
            Row {
                Button(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                Button(onClick = onConfirm) { Text(stringResource(R.string.delete)) }
            }
        }
    }
}
```

## Alert dialog — for confirmations and errors

For simple primary + optional ghost button dialogs, use the `GitHubExplorerAlertDialog` variant instead of building a custom body:

```kotlin
GitHubExplorerAlertDialog(
    title = stringResource(R.string.confirm_delete_title),
    message = stringResource(R.string.confirm_delete_message, itemName),
    primaryButtonText = stringResource(R.string.delete),
    onPrimaryClick = onConfirm,
    ghostButtonText = stringResource(R.string.cancel),
    onGhostClick = onDismiss,
    onDismiss = onDismiss,
)
```

Error dialogs specifically use `GitHubExplorerErrorAlertDialog` — same shape but takes a `UiText` message directly (see `rules/error-handling.md`).

## Hard rules

- **`GheDialogContainer` is the outermost wrapper** — no `Dialog { }`, no `Surface { }`, no `Box(modifier = Modifier.width(...))` on top of it
- **Dialogs never nest a `GheScaffold`** — same reason two Scaffolds shouldn't nest
- **Dialog state lives in the ViewModel or a delegate** — the dialog composable receives `visible: Boolean` (or is conditionally rendered) and dispatches events on primary/dismiss
- **Confirmation and error dialogs use the AlertDialog variant** — do not build a custom body when the standard variant fits
- **Dialogs are `internal`** and live in `presentation/ui/components/dialogs/` (globally reused) or `presentation/ux/<feature>/components/` (feature-specific)

## Conditional rendering

```kotlin
if (uiState.showDeleteDialog) {
    ConfirmDeleteDialog(
        itemName = uiState.itemName,
        onConfirm = { handleEvent(<Screen>Event.OnDeleteConfirmed) },
        onDismiss = { handleEvent(<Screen>Event.OnDeleteDismissed) },
    )
}
```

Prefer the `if (visible) { Dialog() }` form over passing `visible` into the dialog and having it early-return. Compose's diff handles this cleanly.

## Common violations

- `Dialog(onDismissRequest = ...) { Surface { ... } }` — replace outer wrappers with `GheDialogContainer`
- Custom width/shape/elevation on a dialog → the tokens live inside `GheDialogContainer`; if you need different tokens, add a variant to `GheDialogContainer` itself
- Dialog in `presentation/ux/<feature>/` used from multiple features → promote to `presentation/ui/components/dialogs/`
- Two-button confirmation dialog with custom layout → use `GitHubExplorerAlertDialog` instead
