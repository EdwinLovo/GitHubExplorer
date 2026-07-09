package edwinlovo.githubexplorer.domain.model.response.repos

data class RepoDetails(
    val id: Long,
    val name: String,
    val fullName: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val language: String?,
    val topics: List<String>,
    val licenseName: String?,
    val updatedAt: String,
    val htmlUrl: String,
)
