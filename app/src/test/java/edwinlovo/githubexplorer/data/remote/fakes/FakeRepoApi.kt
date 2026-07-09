package edwinlovo.githubexplorer.data.remote.fakes

import edwinlovo.githubexplorer.data.remote.api.RepoApi
import edwinlovo.githubexplorer.domain.model.response.repos.LicenseDto
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetailsDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeRepoApi : RepoApi {
    var response: Response<RepoDetailsDto> = Response.success(sampleDto())
    var exception: Exception? = null

    var lastOwner: String? = null
        private set
    var lastRepo: String? = null
        private set

    override suspend fun getRepository(owner: String, repo: String): Response<RepoDetailsDto> {
        lastOwner = owner
        lastRepo = repo
        exception?.let { throw it }
        return response
    }

    companion object {
        fun sampleDto(id: Long = 1L, name: String = "repo"): RepoDetailsDto = RepoDetailsDto(
            id = id,
            name = name,
            fullName = "owner/$name",
            description = "desc",
            stargazersCount = 100,
            forksCount = 10,
            subscribersCount = 5,
            openIssuesCount = 2,
            language = "Kotlin",
            topics = listOf("android"),
            license = LicenseDto(name = "MIT"),
            updatedAt = "2024-01-01T00:00:00Z",
            htmlUrl = "https://github.com/owner/$name",
            owner = GithubOwnerDto(login = "owner", avatarUrl = "https://a.example/o.png"),
        )

        fun errorResponse(code: Int): Response<RepoDetailsDto> =
            Response.error(code, "".toResponseBody("application/json".toMediaType()))
    }
}
