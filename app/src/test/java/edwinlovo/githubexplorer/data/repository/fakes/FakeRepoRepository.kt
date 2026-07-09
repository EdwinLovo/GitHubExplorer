package edwinlovo.githubexplorer.data.repository.fakes

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.domain.repository.RepoRepository
import edwinlovo.githubexplorer.domain.utils.ApiError
import edwinlovo.githubexplorer.domain.utils.ApiResult
import edwinlovo.githubexplorer.domain.utils.ApiSuccess

class FakeRepoRepository(
    var result: ApiResult<RepoDetails> = ApiSuccess(sampleDetails()),
) : RepoRepository {

    private val _requests = mutableListOf<Pair<String, String>>()
    val requests: List<Pair<String, String>> get() = _requests.toList()

    override suspend fun getRepository(owner: String, repo: String): ApiResult<RepoDetails> {
        _requests += owner to repo
        return result
    }

    fun setError(code: Int?, message: String? = null) {
        result = ApiError(code = code, message = message)
    }

    fun setSuccess(details: RepoDetails = sampleDetails()) {
        result = ApiSuccess(details)
    }

    companion object {
        fun sampleDetails(
            id: Long = 1L,
            name: String = "compose",
            ownerLogin: String = "google",
        ): RepoDetails = RepoDetails(
            id = id,
            name = name,
            fullName = "$ownerLogin/$name",
            ownerLogin = ownerLogin,
            ownerAvatarUrl = "https://a.example/$ownerLogin.png",
            description = "desc",
            stargazersCount = 100,
            forksCount = 20,
            watchersCount = 5,
            openIssuesCount = 3,
            language = "Kotlin",
            topics = listOf("android"),
            licenseName = "MIT",
            updatedAt = "2025-01-01T00:00:00Z",
            htmlUrl = "https://github.com/$ownerLogin/$name",
        )
    }
}
