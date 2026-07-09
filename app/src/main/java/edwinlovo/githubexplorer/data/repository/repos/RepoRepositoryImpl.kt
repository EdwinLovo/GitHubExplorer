package edwinlovo.githubexplorer.data.repository.repos

import edwinlovo.githubexplorer.data.mappers.repos.toRepoDetails
import edwinlovo.githubexplorer.data.remote.api.RepoApi
import edwinlovo.githubexplorer.data.repository.BaseRepository
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.domain.repository.RepoRepository
import edwinlovo.githubexplorer.domain.utils.ApiResult
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: RepoApi,
) : BaseRepository(), RepoRepository {

    override suspend fun getRepository(owner: String, repo: String): ApiResult<RepoDetails> =
        safeCallSuspend(
            call = { api.getRepository(owner, repo) },
            mapper = { it.toRepoDetails() },
        )
}
