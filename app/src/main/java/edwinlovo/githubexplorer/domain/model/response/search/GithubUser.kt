package edwinlovo.githubexplorer.domain.model.response.search

data class GithubUser(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val type: AccountType,
)

enum class AccountType { USER, ORGANIZATION }
