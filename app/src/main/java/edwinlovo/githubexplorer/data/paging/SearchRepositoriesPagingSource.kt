package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingState
import edwinlovo.githubexplorer.data.mappers.search.toGithubRepo
import edwinlovo.githubexplorer.data.remote.api.SearchApi
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo

class SearchRepositoriesPagingSource(
    private val api: SearchApi,
    private val query: String,
) : BasePagingSource<Int, GithubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> = safeLoad {
        val page = params.key ?: STARTING_PAGE
        val isDefaultFeed = query.isBlank()
        val response = api.searchRepositories(
            query = if (isDefaultFeed) POPULAR_QUERY else query,
            sort = if (isDefaultFeed) SORT_STARS else null,
            order = if (isDefaultFeed) ORDER_DESC else null,
            page = page,
            perPage = PAGE_SIZE,
        )
        val items = response.items.map { it.toGithubRepo() }
        val effectiveTotal = minOf(response.totalCount, MAX_SEARCH_RESULTS)
        val loadedCount = page * PAGE_SIZE
        val nextKey = if (items.isEmpty() || loadedCount >= effectiveTotal) null else page + 1
        val prevKey = if (page == STARTING_PAGE) null else page - 1
        LoadResult.Page(data = items, prevKey = prevKey, nextKey = nextKey)
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? =
        state.anchorPosition?.let { anchor ->
            val closest = state.closestPageToPosition(anchor)
            closest?.prevKey?.plus(1) ?: closest?.nextKey?.minus(1)
        }

    companion object {
        const val PAGE_SIZE = 30
        private const val STARTING_PAGE = 1
        private const val MAX_SEARCH_RESULTS = 1000
        private const val POPULAR_QUERY = "stars:>10000"
        private const val SORT_STARS = "stars"
        private const val ORDER_DESC = "desc"
    }
}
