package edwinlovo.githubexplorer.domain.repository

import androidx.paging.PagingData
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchRepositories(query: String): Flow<PagingData<GithubRepo>>
}
