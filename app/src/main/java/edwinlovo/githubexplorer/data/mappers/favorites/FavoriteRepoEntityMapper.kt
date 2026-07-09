package edwinlovo.githubexplorer.data.mappers.favorites

import edwinlovo.githubexplorer.data.local.FavoriteRepoEntity
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo

fun FavoriteRepoEntity.toFavoriteRepo(): FavoriteRepo = FavoriteRepo(
    id = id,
    name = name,
    ownerLogin = ownerLogin,
    ownerAvatarUrl = ownerAvatarUrl,
    description = description,
    stargazersCount = stargazersCount,
    language = language,
)

fun FavoriteRepo.toEntity(): FavoriteRepoEntity = FavoriteRepoEntity(
    id = id,
    name = name,
    ownerLogin = ownerLogin,
    ownerAvatarUrl = ownerAvatarUrl,
    description = description,
    stargazersCount = stargazersCount,
    language = language,
)
