---
name: icons
description: All icons come from the central GheIcons registry; never Material Icons; add drawable + register
paths:
  - "**/presentation/**/*.kt"
---

# Icons

All icons come from `GheIcons` (`presentation/utils/GheIcons.kt`). **`androidx.compose.material.icons.Icons.*` is banned** in UI code — the goal is one shared visual vocabulary controlled by design, not the Material default set.

**Setting up the `GheIcons` registry in a new project:** invoke the `misc-primitives` skill (`.claude/skills/misc-primitives/SKILL.md`). It ships the `GheIcons.kt` stub under `presentation/utils/`.

## Adding a new icon

1. Get the SVG or Vector Drawable XML from design.
2. Save it as `app/src/main/res/drawable/<icon_name>.xml`.
3. Register it in `GheIcons`:

```kotlin
// presentation/utils/GheIcons.kt
object GheIcons {
    val Add = R.drawable.ic_add
    val Delete = R.drawable.ic_delete
    val CheckCircle = R.drawable.ic_check_circle
    val Search = R.drawable.ic_search
    // ...
}
```

Names in `GheIcons` are PascalCase and describe the icon, not the use case (`Trash` not `RemoveItemIcon`).

## Using an icon

**Tinted vector icon** — use `Icon()` with `painterResource(GheIcons.X)`:

```kotlin
Icon(
    painter = painterResource(GheIcons.Delete),
    contentDescription = stringResource(R.string.delete),
    tint = GheTheme.colors.onSurface,
)
```

`Icon` applies the tint, so the vector should typically have `android:tint="?attr/colorControlNormal"` or a solid black fill so the tint takes effect.

**Full-color raster / PNG** — use `Image()` (no tint):

```kotlin
Image(
    painter = painterResource(GheIcons.LogoFull),
    contentDescription = stringResource(R.string.app_logo),
)
```

## Content descriptions

Icons that convey meaning have a non-null `contentDescription` sourced from `stringResource`. Purely decorative icons (icon next to a labeled text button) pass `contentDescription = null`:

```kotlin
// Meaningful — needed by TalkBack
Icon(painter = painterResource(GheIcons.Delete), contentDescription = stringResource(R.string.delete))

// Decorative — a text label already exists next to it
Row {
    Icon(painter = painterResource(GheIcons.Add), contentDescription = null)
    Text(text = stringResource(R.string.add))
}
```

## Size

Prefer `GheTheme.iconSize.<size>` over inline `.dp`:

```kotlin
Icon(
    painter = painterResource(GheIcons.Search),
    contentDescription = null,
    modifier = Modifier.size(GheTheme.iconSize.medium),
)
```

## Hard rules

- **No `Icons.Default.*`, `Icons.Filled.*`, `Icons.Outlined.*`, `Icons.Rounded.*`** — grep for `androidx.compose.material.icons.` and remove
- **No `androidx.compose.material.icons.Icons` import** — banned in `presentation/`
- **Every new drawable is registered in `GheIcons`** — inline `painterResource(R.drawable.ic_x)` scattered around the codebase is a violation; use `GheIcons.X`
- **Content description from `stringResource` or explicitly `null`** — never a literal string
- **Size via `GheTheme.iconSize.<size>`** — inline `24.dp` is a violation (see `rules/theming-and-tokens.md`)

## Common violations

- `Icon(imageVector = Icons.Default.Delete, ...)` → replace with `Icon(painter = painterResource(GheIcons.Delete), ...)`
- `painterResource(R.drawable.ic_delete)` at a call site with no `GheIcons` entry → add `Delete = R.drawable.ic_delete` to `GheIcons` and reference `GheIcons.Delete`
- `contentDescription = "Delete"` → `contentDescription = stringResource(R.string.delete)`
- Meaningful icon with `contentDescription = null` → add a resource; TalkBack users can't perceive it otherwise
- `Icon` used for a colored raster (loses colors to tint) → use `Image` instead
