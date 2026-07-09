package edwinlovo.githubexplorer.data.mappers.search

import edwinlovo.githubexplorer.domain.model.response.search.AccountType
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.domain.model.response.search.GithubUserDto

fun GithubUserDto.toGithubUser(): GithubUser = GithubUser(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type.toAccountType(),
)

private fun String.toAccountType(): AccountType = when (this.uppercase()) {
    "ORGANIZATION" -> AccountType.ORGANIZATION
    else -> AccountType.USER
}
