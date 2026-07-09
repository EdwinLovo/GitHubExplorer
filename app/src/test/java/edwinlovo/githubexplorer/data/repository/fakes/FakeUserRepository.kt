package edwinlovo.githubexplorer.data.repository.fakes

import androidx.paging.PagingData
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.domain.repository.UserRepository
import edwinlovo.githubexplorer.domain.utils.ApiError
import edwinlovo.githubexplorer.domain.utils.ApiResult
import edwinlovo.githubexplorer.domain.utils.ApiSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserRepository(
    var result: ApiResult<UserProfile> = ApiSuccess(sampleProfile()),
    var repos: List<GithubRepo> = emptyList(),
) : UserRepository {

    private val _requests = mutableListOf<String>()
    val requests: List<String> get() = _requests.toList()

    override suspend fun getUser(username: String): ApiResult<UserProfile> {
        _requests += username
        return result
    }

    override fun getUserRepos(username: String): Flow<PagingData<GithubRepo>> =
        flowOf(PagingData.from(repos))

    fun setError(code: Int?, message: String? = null) {
        result = ApiError(code = code, message = message)
    }

    fun setSuccess(profile: UserProfile = sampleProfile()) {
        result = ApiSuccess(profile)
    }

    companion object {
        fun sampleProfile(
            id: Long = 1L,
            login: String = "torvalds",
        ): UserProfile = UserProfile(
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
    }
}
