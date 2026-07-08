package edwinlovo.githubexplorer.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import edwinlovo.githubexplorer.data.paging.SearchRepositoriesPagingSource
import edwinlovo.githubexplorer.data.remote.api.SearchApi
import edwinlovo.githubexplorer.data.repository.BaseRepository
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi,
) : BaseRepository(), SearchRepository {

    override fun searchRepositories(query: String): Flow<PagingData<GithubRepo>> =
        Pager(
            config = PagingConfig(
                pageSize = SearchRepositoriesPagingSource.PAGE_SIZE,
                initialLoadSize = SearchRepositoriesPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { SearchRepositoriesPagingSource(api, query) },
        ).flow
}
