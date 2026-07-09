package edwinlovo.githubexplorer.domain.model.favorites

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails

fun RepoDetails.toFavoriteRepo(): FavoriteRepo = FavoriteRepo(
    id = id,
    name = name,
    ownerLogin = ownerLogin,
    ownerAvatarUrl = ownerAvatarUrl,
    description = description,
    stargazersCount = stargazersCount,
    language = language,
)
