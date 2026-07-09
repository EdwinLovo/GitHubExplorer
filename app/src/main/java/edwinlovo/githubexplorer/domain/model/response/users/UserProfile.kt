package edwinlovo.githubexplorer.domain.model.response.users

data class UserProfile(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val name: String?,
    val bio: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val followers: Int,
    val following: Int,
    val publicRepos: Int,
)
