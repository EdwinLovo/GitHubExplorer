package edwinlovo.githubexplorer.domain.repository

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.domain.utils.ApiResult

interface RepoRepository {
    suspend fun getRepository(owner: String, repo: String): ApiResult<RepoDetails>
}
