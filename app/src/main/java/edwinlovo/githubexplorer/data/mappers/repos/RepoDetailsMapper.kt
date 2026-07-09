package edwinlovo.githubexplorer.data.mappers.repos

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetailsDto

fun RepoDetailsDto.toRepoDetails(): RepoDetails = RepoDetails(
    id = id,
    name = name,
    fullName = fullName,
    ownerLogin = owner.login,
    ownerAvatarUrl = owner.avatarUrl,
    description = description,
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    watchersCount = subscribersCount,
    openIssuesCount = openIssuesCount,
    language = language,
    topics = topics,
    licenseName = license?.name,
    updatedAt = updatedAt,
    htmlUrl = htmlUrl,
)
