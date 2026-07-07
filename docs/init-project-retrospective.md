# `/init-project` Retrospective — GitHubExplorer (2026-07-07)

Recap of every problem hit while running `/init-project` (the `project-scaffold` skill) on a **brand-new Android Studio project**, the fixes applied, and recommendations to polish the `android-claude-toolkit` so a fresh scaffold builds and runs out of the box.

## Session summary

- **Environment:** AGP **9.2.1** (built-in Kotlin, no `kotlin-android` plugin), Kotlin **2.2.10**, Gradle 9.x, Compose BOM 2026.02.01, brand-new Android Studio template project.
- **Scaffold choices:** Retrofit, Paging 3 = yes, preview infra = yes, ErrorEventBus = yes.
- **Scaffold output:** 30 files (theme 7, navigation 3, repository 3, utilities 9, components 4, app shell 5 minus deleted stock files, DI 2). Stock template files (root `MainActivity.kt`, `ui/theme/*`) were deleted.
- **End state:** after the fixes below, `./gradlew :app:assembleDebug` is green and the app launches into a Home stub screen.

**Bottom line:** the scaffold itself was correct, but the project did not *compile* (Gradle wiring is out of scope by design) and then did not *run* (`TODO()` start destination). Two template bugs and two AGP 9 environment pitfalls added friction.

---

## Problems encountered

### P1 — Scaffold doesn't compile out of the box (missing Gradle wiring)

**Symptom** — `:app:compileDebugKotlin` fails with ~120 unresolved references across every scaffolded layer:

```
e: GitHubExplorerApplication.kt:4:8 Unresolved reference 'dagger'.
e: ApiModule.kt:10:8 Unresolved reference 'retrofit2'.
e: BasePagingSource.kt:3:17 Unresolved reference 'paging'.
e: AppNavHost.kt:6:17 Unresolved reference 'navigation'.
e: UiText.kt:24:78 Unresolved reference 'generic_error'.
```

**Root cause** — `project-scaffold` deliberately never touches `build.gradle.kts` / `libs.versions.toml`; all dependencies, plugins, and the compiler flag are deferred to a manual checklist printed at the end. On a from-zero project **every single item** on that checklist is required before anything compiles, so skipping it is guaranteed failure.

**Fix applied** — added to `gradle/libs.versions.toml` + root/app `build.gradle.kts`: Hilt + KSP + kotlin-serialization plugins; Hilt, Navigation Compose, lifecycle-compose, kotlinx-serialization, coroutines, Retrofit + converter, OkHttp, Timber, Paging, test libs (Turbine, Truth, coroutines-test); explicit-backing-fields compiler arg.

**Toolkit recommendation** — the highest-impact change:
- Have `/init-project` **write the Gradle wiring itself** (version catalog entries + plugin blocks + dependencies), keeping only truly project-specific items (signing, flavors) manual; or at minimum
- End the scaffold by **running `./gradlew :app:compileDebugKotlin`** and surfacing the result, so "scaffold complete ✅" is never printed over a broken build.

### P2 — Hilt Gradle plugin incompatible with AGP 9

**Symptom:**

```
An exception occurred applying plugin request [id: 'com.google.dagger.hilt.android', version: '2.57.2']
> Failed to apply plugin 'com.google.dagger.hilt.android'.
   > Android BaseExtension not found.
```

**Root cause** — AGP 9 removed the legacy `BaseExtension`; the Hilt Gradle plugin only supports AGP 9 from **Dagger 2.59** (which also *requires* AGP 9 + Gradle 9.1+). See [dagger#5083](https://github.com/google/dagger/issues/5083) and the [2.59 release notes](https://github.com/google/dagger/releases/tag/dagger-2.59).

**Fix applied** — bumped `hilt = "2.60.1"` in `libs.versions.toml`.

**Toolkit recommendation** — add a minimum-version matrix to the `project-scaffold` checklist: *AGP 9+ requires Hilt ≥ 2.59*.

### P3 — KSP vs AGP 9 built-in Kotlin

**Symptom:**

```
> Using kotlin.sourceSets DSL to add Kotlin sources is not allowed with built-in Kotlin.
  Kotlin source set 'debug' contains: [.../build/generated/ksp/debug/kotlin, ...]
  To suppress this error, set android.disallowKotlinSourceSets=false in gradle.properties.
```

**Root cause** — KSP still registers generated sources via the old `kotlin.sourceSets` DSL, which AGP 9's built-in Kotlin disallows by default.

**Fix applied** — `android.disallowKotlinSourceSets=false` in `gradle.properties` (the documented escape hatch: https://developer.android.com/r/tools/built-in-kotlin).

**Toolkit recommendation** — checklist entry: *on AGP 9+ with KSP, add `android.disallowKotlinSourceSets=false` to `gradle.properties`* (drop it once KSP migrates).

### P4 — `baseUrl(TODO(...))` template doesn't compile

**Symptom:**

```
e: ApiModule.kt:31:14 Overload resolution ambiguity between candidates:
fun baseUrl(p0: URL!): Retrofit.Builder!
fun baseUrl(p0: String!): Retrofit.Builder!
fun baseUrl(p0: HttpUrl!): Retrofit.Builder!
```

**Root cause** — the Retrofit `ApiModule` template puts `TODO("BuildConfig.SERVER_URL or similar")` directly in the `baseUrl(...)` argument. `TODO()` returns `Nothing`, which matches all three overloads → the *template itself* can never compile.

**Fix applied** — `private const val BASE_URL = "https://api.github.com/"` at file level in `data/di/ApiModule.kt`.

**Toolkit recommendation** — templates must never place `TODO()` in an argument position with overloads. Use a placeholder constant (`private const val BASE_URL = "https://CHANGE-ME/"`) with a `// TODO` comment instead.

### P5 — Missing string resources for the error dialog

**Symptom:**

```
e: GitHubExplorerErrorAlertDialog.kt:15:41 Unresolved reference 'error_alert_title'.
e: GitHubExplorerErrorAlertDialog.kt:17:53 Unresolved reference 'error_alert_dismiss'.
e: UiText.kt:24:78 Unresolved reference 'generic_error'.
```

**Root cause** — the `project-scaffold` checklist mentions only `generic_error`, but `component-primitives` (`GitHubExplorerErrorAlertDialog`) also requires `error_alert_title` and `error_alert_dismiss` (documented only inside that sub-skill's body).

**Fix applied** — added all three strings to `res/values/strings.xml`.

**Toolkit recommendation** — since the skills already write Kotlin files, they should also **write the string resources they depend on** (append to `strings.xml`). At minimum, the init checklist must list all three.

### P6 — Guaranteed launch crash: `startDestination = TODO(...)`

**Symptom:**

```
FATAL EXCEPTION: main
kotlin.NotImplementedError: An operation is not implemented: pick a start route, e.g. HomeRoute
    at ...presentation.ux.main.MainActivity.onCreate$lambda$1$lambda$0(MainActivity.kt:23)
```

**Root cause** — by design, `/init-project` scaffolds no feature screens, so `MainActivity` ships with `startDestination = TODO(...)`. The scaffold compiles (after P1–P5) but **crashes on first launch, always**.

**Fix applied** — scaffolded a minimal Home screen per `screen-anatomy` (`HomeRoute`, `contracts/HomeContract`, `HomeViewModel`, `HomeScreen` + preview), registered it in `AppNavHost`, set `startDestination = HomeRoute`.

**Toolkit recommendation** — `/init-project` should scaffold a minimal Home stub screen (or automatically chain into `/new-screen` as its final step) so the fresh project *runs*. A start-screen name question fits naturally in the existing question rounds.

### P7 — Renamed compiler flag for explicit backing fields

**Symptom:**

```
w: Flag is not supported by this version of the compiler: -Xexplicit-backing-fields
e: HomeViewModel.kt:17:9 The feature "explicit backing fields" is experimental and should be
   enabled explicitly. This can be done by supplying the compiler argument
   '-XXLanguage:+ExplicitBackingFields'
```

**Root cause** — Kotlin 2.2 renamed the flag. The toolkit (project-scaffold checklist, CLAUDE.md template) still says `-Xexplicit-backing-fields`, which Kotlin ≤ 2.1 used.

**Fix applied** — `freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")` in `app/build.gradle.kts`; CLAUDE.md corrected.

**Toolkit recommendation** — replace the flag in **every** toolkit mention (`.claude/skills/project-scaffold/SKILL.md` checklist step, CLAUDE.md template), noting both spellings by Kotlin version.

### P8 — `BasePagingSource` primitive is Apollo-only

**Root cause** — `repository-primitives` only ships an Apollo `BasePagingSource` ("Only ship this file if the project uses Paging 3 **with Apollo**"). This project chose Retrofit + Paging, a combination the skill doesn't cover.

**Fix applied** — improvised a Retrofit-adapted variant in `data/paging/BasePagingSource.kt`: a `safeLoad { }` helper translating `IOException`/`HttpException` into `LoadResult.Error` (rethrowing `CancellationException`).

**Toolkit recommendation** — add an official Retrofit variant to `.claude/skills/repository-primitives/SKILL.md`, mirroring the existing Apollo/Retrofit split of `BaseRepository`.

### Minor notes

- **Stock template files conflict** — a fresh Android Studio project ships root `MainActivity.kt` + `ui/theme/{Color,Theme,Type}.kt`; they duplicate/conflict with the scaffold and must be deleted. `project-scaffold` should list them explicitly as a deletion step (with confirmation).
- **Manifest needs two edits, checklist mentions one** — besides `android:name=".GitHubExplorerApplication"`, the existing `<activity android:name=".MainActivity">` entry must change to `.presentation.ux.main.MainActivity` because the scaffold moves the activity. Missing this fails the launch even with a green build.

---

## Working version matrix (verified green on this project)

| Component | Version | Note |
|---|---|---|
| AGP | 9.2.1 | built-in Kotlin (no `kotlin-android` plugin) |
| Kotlin | 2.2.10 | flag: `-XXLanguage:+ExplicitBackingFields` |
| Hilt / Dagger | **2.60.1** | ≥ 2.59 required for AGP 9 |
| KSP | 2.2.10-2.0.2 | needs `android.disallowKotlinSourceSets=false` |
| Navigation Compose | 2.9.3 | |
| hilt-navigation-compose | 1.3.0 | |
| lifecycle-*-compose | 2.9.2 | `collectAsStateWithLifecycle` |
| kotlinx-serialization-json | 1.9.0 | |
| kotlinx-coroutines | 1.10.2 | |
| Retrofit + converter-kotlinx-serialization | 3.0.0 | |
| OkHttp | 5.1.0 | |
| Paging (runtime + compose) | 3.3.6 | |
| Timber | 5.0.1 | |
| Turbine / Truth | 1.2.1 / 1.4.4 | tests |

---

## Prioritized action list for the toolkit

| # | Action | File(s) to change | Why first |
|---|---|---|---|
| 1 | Automate Gradle wiring (or verify-build step at end of init) | `.claude/skills/project-scaffold/SKILL.md` | P1 — breaks *every* fresh project |
| 2 | Scaffold a Home stub / chain `/new-screen` so the app runs | `.claude/skills/project-scaffold/SKILL.md` | P6 — guaranteed first-launch crash |
| 3 | Fix explicit-backing-fields flag name everywhere | `project-scaffold/SKILL.md`, CLAUDE.md template | P7 — template bug, silent until first ViewModel |
| 4 | Replace `baseUrl(TODO(...))` with placeholder constant | `.claude/skills/project-scaffold/SKILL.md` (ApiModule templates) | P4 — template can never compile |
| 5 | Write required string resources from the skills (or list all 3) | `misc-primitives`, `component-primitives`, `project-scaffold` checklists | P5 |
| 6 | Document AGP 9 pitfalls: Hilt ≥ 2.59, `disallowKotlinSourceSets` | `.claude/skills/project-scaffold/SKILL.md` checklist | P2/P3 — env-specific, hard to debug |
| 7 | Add Retrofit `BasePagingSource` variant | `.claude/skills/repository-primitives/SKILL.md` | P8 |
| 8 | Add stock-file deletion + full manifest edits to init steps | `.claude/skills/project-scaffold/SKILL.md` | minor notes |

Optionally, pin the version matrix above as the toolkit's suggested defaults in the checklist so a from-zero run doesn't have to rediscover compatible versions.
