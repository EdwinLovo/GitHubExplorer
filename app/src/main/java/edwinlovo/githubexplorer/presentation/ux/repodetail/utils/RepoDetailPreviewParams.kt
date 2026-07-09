package edwinlovo.githubexplorer.presentation.ux.repodetail.utils

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails

internal fun previewRepoDetails(): RepoDetails = RepoDetails(
    id = 1L,
    name = "compose",
    fullName = "google/compose",
    ownerLogin = "google",
    ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1342004",
    description = "The official Jetpack Compose samples repository.",
    stargazersCount = 20_912,
    forksCount = 4_732,
    watchersCount = 380,
    openIssuesCount = 42,
    language = "Kotlin",
    topics = listOf("android", "compose", "jetpack", "kotlin"),
    licenseName = "Apache License 2.0",
    updatedAt = "2025-11-01T12:34:56Z",
    htmlUrl = "https://github.com/google/compose",
)
