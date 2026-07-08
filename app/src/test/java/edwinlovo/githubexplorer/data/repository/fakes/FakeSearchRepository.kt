package edwinlovo.githubexplorer.data.repository.fakes

import androidx.paging.PagingData
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSearchRepository(
    var repos: List<GithubRepo> = emptyList(),
) : SearchRepository {

    private val _requestedQueries = mutableListOf<String>()
    val requestedQueries: List<String> get() = _requestedQueries.toList()

    override fun searchRepositories(query: String): Flow<PagingData<GithubRepo>> {
        _requestedQueries += query
        return flowOf(PagingData.from(repos))
    }
}
