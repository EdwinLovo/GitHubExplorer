package edwinlovo.githubexplorer.data.repository.users

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import edwinlovo.githubexplorer.data.mappers.users.toUserProfile
import edwinlovo.githubexplorer.data.paging.UserReposPagingSource
import edwinlovo.githubexplorer.data.remote.api.UserApi
import edwinlovo.githubexplorer.data.repository.BaseRepository
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.domain.repository.UserRepository
import edwinlovo.githubexplorer.domain.utils.ApiResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
) : BaseRepository(), UserRepository {

    override suspend fun getUser(username: String): ApiResult<UserProfile> =
        safeCallSuspend(
            call = { api.getUser(username) },
            mapper = { it.toUserProfile() },
        )

    override fun getUserRepos(username: String): Flow<PagingData<GithubRepo>> =
        Pager(
            config = PagingConfig(
                pageSize = UserReposPagingSource.PAGE_SIZE,
                initialLoadSize = UserReposPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { UserReposPagingSource(api, username) },
        ).flow
}
