package edwinlovo.githubexplorer.domain.model.response.repos

import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsDto(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val description: String?,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("forks_count") val forksCount: Int,
    @SerialName("subscribers_count") val subscribersCount: Int,
    @SerialName("open_issues_count") val openIssuesCount: Int,
    val language: String?,
    val topics: List<String> = emptyList(),
    val license: LicenseDto?,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("html_url") val htmlUrl: String,
    val owner: GithubOwnerDto,
)

@Serializable
data class LicenseDto(
    val name: String,
)
