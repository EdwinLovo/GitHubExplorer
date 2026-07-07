# GitHub Explorer — Product Requirements Document

**Version:** 1.0
**Date:** 2026-07-07
**Status:** Draft

---

## 1. Overview

GitHub Explorer is an Android app for browsing and searching GitHub repositories and users through the **free (unauthenticated) GitHub REST API**. No sign-in is required. Users can search repositories, discover popular projects, look up developers, inspect repository and profile details, and save favorite repositories locally on the device.

## 2. Goals

- Let users search and discover GitHub repositories and users quickly, without an account.
- Provide rich detail views for repositories and user profiles.
- Let users keep a local list of favorite repositories that persists across app launches.
- Exercise a complete navigation structure: 3 bottom-navigation tabs and 5 screens, including pushed detail screens with back navigation.

### Non-Goals (v1)

- GitHub authentication (OAuth, personal access tokens).
- Write operations against GitHub (starring, following, creating issues/PRs).
- Offline-first sync or caching of API data beyond local favorites.
- Notifications, code browsing, or file viewing.

## 3. Target User

Developers and tech enthusiasts who casually explore open-source projects: checking a library's popularity, finding trending repositories, or looking up a developer's work.

## 4. Navigation Structure

Bottom navigation bar with **3 tabs**. Detail screens are **pushed** on top of the current tab with standard back navigation. Each tab preserves its own back stack when switching tabs.

```
Bottom Navigation
├── Tab 1: Explore
│   └── Repo Detail (push)
│       └── User Profile (push, via owner tap)
├── Tab 2: Users
│   └── User Profile (push)
│       └── Repo Detail (push, via repo tap)
└── Tab 3: Favorites
    └── Repo Detail (push)
```

**Screen inventory (5):** Explore, Repo Detail, Users, User Profile, Favorites.

## 5. Screens

### S1 — Explore (Tab 1)

**Purpose:** Entry point of the app. Search repositories and discover popular projects.

**Content & behavior:**
- Search bar at the top for repository search (debounced as the user types).
- **Default state (no query):** shows popular repositories (repository search sorted by stars).
- Result list items: repo name, owner login + avatar, short description, star count, primary language.
- Infinite scroll pagination on results.
- Tap a repo → **Repo Detail (S2)**.

**States:** loading (initial + pagination), results, empty ("No repositories found for '<query>'"), error (network / rate limit) with retry.

### S2 — Repo Detail (pushed)

**Purpose:** Full detail of a single repository.

**Content & behavior:**
- Header: repo name, owner login + avatar, description.
- Stats: stars, forks, watchers, open issues.
- Metadata: primary language, topics/tags, license, last updated date.
- **Favorite toggle:** add/remove this repo from local Favorites (persisted on device).
- Tap owner → **User Profile (S4)**.
- "Open in browser" action that opens the repo's GitHub page.
- Back navigation to the previous screen.

**States:** loading, loaded, error with retry.

### S3 — Users (Tab 2)

**Purpose:** Search for GitHub users and organizations.

**Content & behavior:**
- Search bar at the top for user search (debounced).
- **Default state (no query):** empty state prompting the user to search ("Search for developers and organizations").
- Result list items: avatar, username, account type (User / Organization).
- Infinite scroll pagination on results.
- Tap a user → **User Profile (S4)**.

**States:** idle (no query), loading, results, empty ("No users found"), error with retry.

### S4 — User Profile (pushed)

**Purpose:** Detail view of a GitHub user or organization.

**Content & behavior:**
- Header: avatar, display name, username, bio.
- Stats: followers, following, public repo count.
- Metadata: location, company, blog/link (when available).
- **Repositories section:** list of the user's public repositories (name, description, stars, language), paginated.
- Tap a repo → **Repo Detail (S2)**.
- Back navigation to the previous screen.

**States:** loading, loaded, error with retry; repo list has its own loading/empty state ("No public repositories").

### S5 — Favorites (Tab 3)

**Purpose:** Locally saved repositories, available across app launches.

**Content & behavior:**
- List of favorited repos: name, owner + avatar, description, stars, language (data stored locally at favorite time).
- Remove a favorite via swipe-to-dismiss or an unfavorite action on the item.
- Tap a repo → **Repo Detail (S2)** (detail refreshed from the API; favorite toggle reflects saved state).
- **Empty state:** friendly message prompting exploration ("No favorites yet — find repos in Explore").

**States:** loaded, empty. No network needed to display the list.

## 6. Features, User Stories & Acceptance Criteria

### F1 — Repository search

> As a user, I want to search repositories by name so I can find projects I'm interested in.

- Typing in the Explore search bar triggers a debounced search (no request per keystroke).
- Results are sorted by relevance; the default (empty query) view shows repos sorted by stars.
- Pagination loads more results as the user scrolls.

### F2 — Repository details

> As a user, I want to see a repository's stats and metadata so I can evaluate the project.

- Repo Detail shows stars, forks, watchers, open issues, language, topics, license, and last updated.
- The owner is tappable and leads to their profile.
- An action opens the repository page in the device browser.

### F3 — User search

> As a user, I want to search for developers and organizations so I can explore their work.

- Debounced user search with paginated results.
- Each result clearly distinguishes users from organizations.

### F4 — User profile

> As a user, I want to view a developer's profile and repositories so I can browse their projects.

- Profile shows bio, follower/following counts, and public repositories.
- Repositories in the profile navigate to Repo Detail.

### F5 — Local favorites

> As a user, I want to save repositories to a favorites list so I can find them again later.

- Favoriting/unfavoriting works from Repo Detail and takes effect immediately.
- Favorites persist across app restarts and are visible without a network connection.
- Removing a favorite from the Favorites list updates immediately.

### F6 — Error & rate-limit handling

> As a user, I want clear feedback when something goes wrong so I know what to do next.

- Network failures show an error state with a retry action.
- When the GitHub rate limit is hit (HTTP 403/429), the app shows a specific, friendly message (e.g., "GitHub request limit reached — try again in a few minutes") instead of a generic error.
- Search input is debounced to minimize unnecessary API calls.

## 7. API Reference (Free Tier)

All requests are unauthenticated calls to `https://api.github.com`.

| Endpoint | Used by |
|---|---|
| `GET /search/repositories?q=...&sort=stars` | S1 Explore (search + default popular list) |
| `GET /search/users?q=...` | S3 Users (search) |
| `GET /repos/{owner}/{repo}` | S2 Repo Detail |
| `GET /users/{username}` | S4 User Profile |
| `GET /users/{username}/repos` | S4 User Profile (repositories section) |

**Rate limits (unauthenticated):**
- Core API: **60 requests/hour** per IP.
- Search API: **10 requests/minute** per IP.

**Implications:** search must be debounced, results should be paginated in reasonable page sizes (e.g., 20–30 items), and rate-limit responses must be surfaced with a clear message (see F6).

## 8. UX Requirements

- Every network-backed screen implements **loading / content / empty / error** states.
- Lists use infinite-scroll pagination with a footer loading indicator.
- Pull-to-refresh on the Explore, Users (with active query), and User Profile screens.
- Light and dark theme support following the system setting.
- Avatars and images load asynchronously with placeholders.
- Back navigation behaves predictably; switching tabs preserves each tab's navigation state.

## 9. Release Scope & Success Criteria (v1)

v1 is complete when:

- [ ] 3 bottom-navigation tabs are functional: Explore, Users, Favorites.
- [ ] All 5 screens are implemented: Explore, Repo Detail, Users, User Profile, Favorites.
- [ ] Repository search with debounce, pagination, and a popular-repos default state works.
- [ ] User search with debounce and pagination works.
- [ ] Repo Detail displays all listed fields and supports the favorite toggle and open-in-browser.
- [ ] User Profile displays profile info and a paginated repo list.
- [ ] Favorites persist across app restarts and display without network.
- [ ] Loading, empty, error, and rate-limit states are handled on every network-backed screen.
- [ ] Navigation: detail screens push/pop correctly and tab back stacks are preserved.
