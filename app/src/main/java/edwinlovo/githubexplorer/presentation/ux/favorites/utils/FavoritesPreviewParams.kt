package edwinlovo.githubexplorer.presentation.ux.favorites.utils

import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo

internal fun previewFavorites(): List<FavoriteRepo> = listOf(
    FavoriteRepo(
        id = 1L,
        name = "kotlin",
        ownerLogin = "JetBrains",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/878437",
        description = "The Kotlin Programming Language.",
        stargazersCount = 47_512,
        language = "Kotlin",
    ),
    FavoriteRepo(
        id = 2L,
        name = "compose-samples",
        ownerLogin = "android",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/32689599",
        description = "Official Jetpack Compose samples.",
        stargazersCount = 20_912,
        language = "Kotlin",
    ),
)
