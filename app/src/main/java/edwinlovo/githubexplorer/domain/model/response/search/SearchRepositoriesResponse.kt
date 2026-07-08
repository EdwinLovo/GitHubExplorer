package edwinlovo.githubexplorer.domain.model.response.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRepositoriesResponse(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    val items: List<GithubRepoDto>,
)

@Serializable
data class GithubRepoDto(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val description: String?,
    @SerialName("stargazers_count") val stargazersCount: Int,
    val language: String?,
    val owner: GithubOwnerDto,
)

@Serializable
data class GithubOwnerDto(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
)
