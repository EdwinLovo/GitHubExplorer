package edwinlovo.githubexplorer.data.mappers.search

import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepoDto

fun GithubRepoDto.toGithubRepo(): GithubRepo = GithubRepo(
    id = id,
    name = name,
    fullName = fullName,
    ownerLogin = owner.login,
    ownerAvatarUrl = owner.avatarUrl,
    description = description,
    stargazersCount = stargazersCount,
    language = language,
)
