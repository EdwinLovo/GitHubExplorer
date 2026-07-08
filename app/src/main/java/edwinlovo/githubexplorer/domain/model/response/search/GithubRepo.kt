package edwinlovo.githubexplorer.domain.model.response.search

data class GithubRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val language: String?,
)
