package edwinlovo.githubexplorer.domain.repository

import androidx.paging.PagingData
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.domain.utils.ApiResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(username: String): ApiResult<UserProfile>
    fun getUserRepos(username: String): Flow<PagingData<GithubRepo>>
}
