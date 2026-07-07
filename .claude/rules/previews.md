---
name: previews
description: Every displayable-UI composable has a preview at the bottom of its file, rendering light + dark; every PreviewParameterProvider lives in a sibling <Name>PreviewParams.kt file
paths:
  - "**/presentation/ui/components/**/*.kt"
  - "**/presentation/ux/**/components/**/*.kt"
  - "**/*Screen.kt"
  - "**/*PreviewParams.kt"
---

# Previews

Every `@Composable` function that renders displayable UI must have a Compose preview at the bottom of its file. Reviewers should see the component in the IDE without running the app.

**Setting up the preview annotations in a new project:** invoke the `misc-primitives` skill (`.claude/skills/misc-primitives/SKILL.md`). It ships `GhePreview.kt` under `presentation/utils/` with the `@GhePreview` component + `@GhePreviewScreen` full-screen annotations and the `GitHubExplorerPreviewContainer` composable.

**Scope of this rule:**
- **Applies to** files under `presentation/ui/components/`, `presentation/ux/<feature>/components/`, and screen files (`<Screen>Screen.kt`)
- **Exempt** utility composables in `presentation/utils/` — extension composables (`UiText.asString()`), effect helpers (`OnLifecycleResumed`, `ObserveAsEvents`), and preview infrastructure itself (`GitHubExplorerPreviewContainer`) don't render UI, so they don't need previews

## Annotations

Two annotations, both defined in `presentation/utils/GhePreview.kt`:

- `@GhePreview` — components (default size, wraps content)
- `@GhePreviewScreen` — full screens (target device size — tablet: `1280 × 800`; phone: e.g. `411 × 892`)

**Both must render light and dark in a single annotation** using `@Preview(uiMode = UI_MODE_NIGHT_YES)` variants. Never dark-only — light comes first.

## Preview container

Every preview wraps the composable in a container that applies the project theme:

```kotlin
@GhePreview
@Composable
private fun ProductRowPreview() {
    GitHubExplorerPreviewContainer {
        ProductRow(product = Product(...), onClick = {})
    }
}
```

The `GitHubExplorerPreviewContainer` composable applies `GheTheme` and a default background.

## Reference implementation

Copy this into `edwinlovo.githubexplorer/presentation/utils/GhePreview.kt`. Every preview in the app assumes these three symbols exist. Adjust `widthDp` / `heightDp` on `@GhePreviewScreen` for your target device (tablet `1280×800`, phone `411×892`, etc.).

```kotlin
package edwinlovo.githubexplorer.presentation.utils

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.ui.theme.GitHubExplorerTheme

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,  backgroundColor = 0xffffffff)
@Preview(name = "Dark",  showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xff000000)
annotation class GhePreview

@Preview(name = "Light — Tablet", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,  backgroundColor = 0xffffffff, widthDp = 1280, heightDp = 800)
@Preview(name = "Dark — Tablet",  showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xff000000, widthDp = 1280, heightDp = 800)
annotation class GhePreviewScreen

@Composable
fun GitHubExplorerPreviewContainer(content: @Composable () -> Unit) {
    GitHubExplorerTheme {
        Column(modifier = Modifier.padding(GheTheme.padding.xs)) {
            content()
        }
    }
}
```

## Naming

- Component preview → `fun <ComponentName>Preview()`
- Screen preview → `fun <ScreenName>ContentPreview()` (previews `<Screen>ScreenContent`, not the entry composable — the entry needs a NavController)

Always `private`.

## Component previews — sibling `<Name>PreviewParams.kt`

**Every `PreviewParameterProvider` for a component lives in a sibling `<Component>PreviewParams.kt` file — always, no in-file exceptions.**

Component `ProductRow.kt` → provider at `ProductRowPreviewParams.kt` in the same folder.

The provider file holds only the `PreviewParameterProvider` implementation(s). Keep it `internal` (or `private` if that works with `@PreviewParameter` in your codebase) — local to the module. The component file imports the provider by name.

This keeps the component file focused on the composable and makes the provider trivially reusable if a second preview needs the same variants.

**Provider file** — `ProductRowPreviewParams.kt`:
```kotlin
internal class ProductRowStateProvider : PreviewParameterProvider<ProductRowState> {
    override val values = sequenceOf(
        ProductRowState.Idle,
        ProductRowState.Selected,
        ProductRowState.Disabled,
    )
}
```

**Component file** — `ProductRow.kt` (bottom, alongside the composable):
```kotlin
@GhePreview
@Composable
private fun ProductRowPreview(
    @PreviewParameter(ProductRowStateProvider::class) state: ProductRowState,
) {
    GitHubExplorerPreviewContainer {
        ProductRow(state = state, onClick = {})
    }
}
```

## Screen previews — hardcode `UiState` inline

Screens don't use `PreviewParameterProvider`. Construct `<Screen>UiState()` inline in each preview and pass only `uiState` and `handleEvent` — no auxiliary data params, no helper `val`s in the preview body. One preview when a single state is enough; one named preview per variant when there are several.

```kotlin
@GhePreviewScreen
@Composable
private fun CatalogScreenContentPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        CatalogScreenContent(
            uiState = CatalogUiState(items = listOf(Product("1", "Apple"), Product("2", "Banana"))),
            handleEvent = {},
        )
    }
}
```

If a `UiState` field value is bulky or repeated, extract a factory to `<feature>/utils/<Feature>PreviewParams.kt` and call it *inside* the constructor: `uiState = CatalogUiState(items = previewCatalogItems())`.

Paging screens (`Flow<PagingData<T>>` — see `rules/viewmodels.md`) additionally receive a `LazyPagingItems<T>` param. That's the only sanctioned non-`uiState` argument.

## Hard rules

- Every composable in `.kt` files under this rule's scope has a preview at the bottom — no missing previews
- Preview functions are always `private`
- Preview renders light + dark (never dark-only, never light-only)
- Wrap in `GitHubExplorerPreviewContainer { }` — applies theme + background
- Screen previews target `<Screen>ScreenContent`, not the entry composable
- Component `PreviewParameterProvider` implementations live in a sibling `<Component>PreviewParams.kt` file — never inline in the component file
- Screens don't use `@PreviewParameter` — they hardcode `<Screen>UiState()` inline in each preview function. One preview for single-state screens, one named preview per variant when there are several
- Screen previews pass only `uiState` and `handleEvent` — everything the screen renders is baked into the `UiState` value; no auxiliary data params, no helper-function calls in the preview body (except the `LazyPagingItems` param for paging screens)
- Screen preview data factories (functions used inside a `UiState(...)` constructor when the value is bulky or repeated) live in `<feature>/utils/<Feature>PreviewParams.kt` — skip the file entirely if hardcoded values fit inside the `UiState()` call
- Prefer the smallest realistic parameter set — 2–3 variants beats a dozen

## Common violations

- Composable without a preview → add one
- `@Preview` used directly (not `@GhePreview`) → migrate to the project annotation for consistent size and light/dark
- Preview without `GitHubExplorerPreviewContainer` → theme won't apply, colors will be wrong
- `PreviewParameterProvider` inlined in a component file → extract to a sibling `<Component>PreviewParams.kt`
- Multiple `PreviewParameterProvider` implementations grouped in a shared `PreviewParameterProviders.kt` catch-all → one provider file per component, named `<Component>PreviewParams.kt`, sitting next to the composable it feeds
- Screen using `@PreviewParameter` with a `<Screen>UiState`-typed provider → drop the provider; construct `<Screen>UiState()` inline in the preview function (one preview per state variant if there are several)
- Screen preview passes auxiliary data alongside `uiState` (e.g. `orders = previewOpenOrders()` as its own param) → move that data into a `UiState` field so the preview only passes `uiState` and `handleEvent`
- Screen preview body calls a helper (`val items = previewProducts()`) before constructing the `UiState` → inline the call inside the `UiState(...)` constructor instead: `uiState = CatalogUiState(items = previewProducts())`
- Bulky preview data (long lists, deep nested values) hand-written inside every preview function → extract a factory to `<feature>/utils/<Feature>PreviewParams.kt` and call it from inside the `UiState(...)` constructor
- Multiple preview functions for the same variants → collapse with `@PreviewParameter`
- `GhePreview.kt` missing from `presentation/utils/` when the rule is referenced → copy the "Reference implementation" block above into `edwinlovo.githubexplorer/presentation/utils/GhePreview.kt`
