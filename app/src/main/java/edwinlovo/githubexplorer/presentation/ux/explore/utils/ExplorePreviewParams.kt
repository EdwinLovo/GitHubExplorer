package edwinlovo.githubexplorer.presentation.ux.explore.utils

import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo

internal fun previewRepos(): List<GithubRepo> = listOf(
    GithubRepo(
        id = 1L,
        name = "compose-samples",
        fullName = "android/compose-samples",
        ownerLogin = "android",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/32689599",
        description = "Official Jetpack Compose samples.",
        stargazersCount = 20_912,
        language = "Kotlin",
    ),
    GithubRepo(
        id = 2L,
        name = "kotlin",
        fullName = "JetBrains/kotlin",
        ownerLogin = "JetBrains",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/878437",
        description = "The Kotlin Programming Language.",
        stargazersCount = 47_512,
        language = "Kotlin",
    ),
    GithubRepo(
        id = 3L,
        name = "coil",
        fullName = "coil-kt/coil",
        ownerLogin = "coil-kt",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/52722434",
        description = "Image loading for Android and Compose Multiplatform.",
        stargazersCount = 11_403,
        language = "Kotlin",
    ),
)
