package edwinlovo.githubexplorer.domain.model.response.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchUsersResponse(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    val items: List<GithubUserDto>,
)

@Serializable
data class GithubUserDto(
    val id: Long,
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val type: String,
)
