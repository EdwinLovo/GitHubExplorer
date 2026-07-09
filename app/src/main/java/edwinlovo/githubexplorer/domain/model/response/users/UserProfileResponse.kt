package edwinlovo.githubexplorer.domain.model.response.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: Long,
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val name: String? = null,
    val bio: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val followers: Int,
    val following: Int,
    @SerialName("public_repos") val publicRepos: Int,
)
