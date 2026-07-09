package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingState
import edwinlovo.githubexplorer.data.mappers.search.toGithubUser
import edwinlovo.githubexplorer.data.remote.api.SearchApi
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser

class SearchUsersPagingSource(
    private val api: SearchApi,
    private val query: String,
) : BasePagingSource<Int, GithubUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> = safeLoad {
        val page = params.key ?: STARTING_PAGE
        val response = api.searchUsers(
            query = query,
            page = page,
            perPage = PAGE_SIZE,
        )
        val items = response.items.map { it.toGithubUser() }
        val effectiveTotal = minOf(response.totalCount, MAX_SEARCH_RESULTS)
        val loadedCount = page * PAGE_SIZE
        val nextKey = if (items.isEmpty() || loadedCount >= effectiveTotal) null else page + 1
        val prevKey = if (page == STARTING_PAGE) null else page - 1
        LoadResult.Page(data = items, prevKey = prevKey, nextKey = nextKey)
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? =
        state.anchorPosition?.let { anchor ->
            val closest = state.closestPageToPosition(anchor)
            closest?.prevKey?.plus(1) ?: closest?.nextKey?.minus(1)
        }

    companion object {
        const val PAGE_SIZE = 30
        private const val STARTING_PAGE = 1
        private const val MAX_SEARCH_RESULTS = 1000
    }
}
