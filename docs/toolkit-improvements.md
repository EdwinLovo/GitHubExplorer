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
