# android-claude-toolkit — Improvements Backlog

Running list of improvements for `android-claude-toolkit` discovered while building **GitHubExplorer**. Each entry records what the toolkit prescribes today, the friction it caused in practice, and what it should say instead. Append new entries as `## I<N>: <title>`.

_Started: 2026-07-09_

---

## I1: Single Retrofit API service with comment-separated sections

**Current toolkit behavior**
The `data-feature` skill (`.claude/skills/data-feature/SKILL.md`, Retrofit variant §3) instructs one Retrofit interface per resource: each service lives in its own file at `data/remote/api/<Resource>Api.kt` and is provided individually by `ApiModule` via `retrofit.create(...)`.

**Problem**
File and DI-provider proliferation. In this repo, two features already produced `SearchApi.kt` and `RepoApi.kt` — each holding a single endpoint — plus two nearly identical `@Provides` methods in `data/di/ApiModule.kt`. Every new resource adds another interface + provider for a client that shares the same base URL and auth context.

**Proposed change**
Prescribe a single API service interface per Retrofit client — e.g. `<App>Api` (`GheApi` here) in `data/remote/api/` — with endpoints grouped into sections separated by comments (e.g. `// region Search` / `// endregion`), and one `retrofit.create(...)` provider in `ApiModule`. When multiple auth contexts exist (`@Named("public")` / `@Named("user")` Retrofit instances), keep one service *per auth context*, not per resource.

**Toolkit files to update**
- `.claude/skills/data-feature/SKILL.md` — Retrofit variant §3 (service location, "one interface per resource" rule, ApiModule wiring)
- `CLAUDE.md` — data-layer patterns section

---

## I2: Shell-owned bottom navigation (official NiA pattern)

**Current toolkit behavior**
`rules/screens.md` mandates that all chrome — including bottom bars — is passed per-screen as `GheScaffold` slot arguments, and its "app-shell exception" forbids the composable hosting `AppNavHost` (`MainScreen`) from owning any scaffold-level chrome. Following this for bottom tabs means every tab screen renders its own copy of the nav bar, hardcodes its own selected tab, and duplicates an `OnTabSelected` event across each tab's contract, ViewModel, and tests.

**Problem**
This diverges from Google's official guidance (Navigation docs, Now in Android): the bar is recreated per destination so it crossfades with screen transitions instead of staying static; selection is hardcoded per screen instead of derived reactively from the back stack; and N tabs means N copies of identical tab-switch wiring. Accessibility is unaffected (Material3 `NavigationBarItem` is used), but the structure fights the framework.

**Proposed change**
Prescribe the official single-NavController shell pattern for apps with bottom navigation:
- The app shell (`MainScreen`) renders the bottom bar in a `Column` beneath `AppNavHost` (bar is NOT a per-screen slot).
- Selected tab derives from `navController.currentBackStackEntryAsState()` + `destination.hierarchy.any { it.hasRoute(tab.route::class) }`; the bar renders only when the current destination matches a tab (detail screens hide it structurally).
- Tab clicks navigate via the shell's ViewModel (`ViewModelNav.navigateToTab(route)`), backed by a `NavigationAction.NavigateToTab` that applies `popUpTo(findStartDestination()) { saveState = true }` + `launchSingleTop` + `restoreState`.
- Tab screens carry no bottom-bar wiring: no bar slot, no `OnTabSelected` event.

**Toolkit files to update**
- `.claude/rules/screens.md` — rewrite the "app-shell exception" and bottom-bar slot guidance: the shell owns the bottom nav bar; `GheScaffold`'s `bottomBar` slot is reserved for non-nav chrome
- `.claude/rules/navigation.md` — document `navigateToTab` / `NavigateToTab` and back-stack-derived tab selection
- `.claude/skills/navigation-primitives/SKILL.md` — ship `NavigateToTab` in the `NavigationAction` reference file
- `.claude/skills/screen-anatomy/SKILL.md` — remove bottom-bar wiring from tab-screen templates
- `CLAUDE.md` — architecture/navigation sections

> Note: GitHubExplorer implements this pattern ahead of the toolkit update, so it intentionally diverges from the local copy of `rules/screens.md` until the toolkit lands the change.
