package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingState
import edwinlovo.githubexplorer.data.mappers.search.toGithubRepo
import edwinlovo.githubexplorer.data.remote.api.UserApi
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo

class UserReposPagingSource(
    private val api: UserApi,
    private val username: String,
) : BasePagingSource<Int, GithubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> = safeLoad {
        val page = params.key ?: STARTING_PAGE
        val response = api.getUserRepos(
            username = username,
            page = page,
            perPage = PAGE_SIZE,
        )
        val items = response.map { it.toGithubRepo() }
        val nextKey = if (items.size < PAGE_SIZE) null else page + 1
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
    }
}
