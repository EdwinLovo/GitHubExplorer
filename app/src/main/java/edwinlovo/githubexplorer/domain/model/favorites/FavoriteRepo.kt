package edwinlovo.githubexplorer.domain.model.favorites

data class FavoriteRepo(
    val id: Long,
    val name: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val language: String?,
)
