package edwinlovo.githubexplorer.domain.model.response.search

import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo

fun FavoriteRepo.toGithubRepo(): GithubRepo = GithubRepo(
    id = id,
    name = name,
    fullName = "$ownerLogin/$name",
    ownerLogin = ownerLogin,
    ownerAvatarUrl = ownerAvatarUrl,
    description = description,
    stargazersCount = stargazersCount,
    language = language,
)
