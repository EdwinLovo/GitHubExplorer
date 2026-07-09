package edwinlovo.githubexplorer.data.remote.fakes

import edwinlovo.githubexplorer.data.remote.api.UserApi
import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepoDto
import edwinlovo.githubexplorer.domain.model.response.users.UserProfileDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeUserApi : UserApi {
    var userResponse: Response<UserProfileDto> = Response.success(sampleDto())
    var reposResponse: List<GithubRepoDto> = emptyList()
    var exception: Exception? = null

    var lastUsername: String? = null
        private set
    var lastReposUsername: String? = null
        private set
    var lastReposPage: Int? = null
        private set
    var lastReposPerPage: Int? = null
        private set

    override suspend fun getUser(username: String): Response<UserProfileDto> {
        lastUsername = username
        exception?.let { throw it }
        return userResponse
    }

    override suspend fun getUserRepos(
        username: String,
        page: Int,
        perPage: Int,
    ): List<GithubRepoDto> {
        lastReposUsername = username
        lastReposPage = page
        lastReposPerPage = perPage
        exception?.let { throw it }
        return reposResponse
    }

    companion object {
        fun sampleDto(id: Long = 1L, login: String = "torvalds"): UserProfileDto = UserProfileDto(
            id = id,
            login = login,
            avatarUrl = "https://a.example/$login.png",
            name = "Test $login",
            bio = "bio",
            company = "co",
            blog = "blog.example",
            location = "loc",
            followers = 100,
            following = 50,
            publicRepos = 10,
        )

        fun sampleRepoDto(id: Long, name: String = "repo-$id"): GithubRepoDto = GithubRepoDto(
            id = id,
            name = name,
            fullName = "owner/$name",
            description = "desc for $name",
            stargazersCount = 100,
            language = "Kotlin",
            owner = GithubOwnerDto(login = "owner-$id", avatarUrl = "https://a.example/$id.png"),
        )

        fun errorResponse(code: Int): Response<UserProfileDto> =
            Response.error(code, "".toResponseBody("application/json".toMediaType()))
    }
}
