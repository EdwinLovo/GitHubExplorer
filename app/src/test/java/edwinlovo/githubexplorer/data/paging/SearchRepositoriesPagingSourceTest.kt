package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.paging.SearchRepositoriesPagingSource.Companion.PAGE_SIZE
import edwinlovo.githubexplorer.data.remote.fakes.FakeSearchApi
import edwinlovo.githubexplorer.data.remote.fakes.FakeSearchApi.Companion.sampleRepo
import edwinlovo.githubexplorer.data.remote.fakes.FakeSearchApi.Companion.sampleResponse
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SearchRepositoriesPagingSourceTest {

    @Test
    fun loadRefreshReturnsPageWithNullPrevAndNextKeyTwo() = runTest {
        val api = FakeSearchApi().apply {
            response = sampleResponse(items = List(PAGE_SIZE) { sampleRepo(it.toLong()) }, totalCount = 5_000)
        }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Page

        assertThat(result.prevKey).isNull()
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.data).hasSize(PAGE_SIZE)
    }

    @Test
    fun loadAppendReturnsAdjacentPrevAndNextKeys() = runTest {
        val api = FakeSearchApi().apply {
            response = sampleResponse(items = List(PAGE_SIZE) { sampleRepo(it.toLong()) }, totalCount = 5_000)
        }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(appendParams(key = 3)) as PagingSource.LoadResult.Page

        assertThat(result.prevKey).isEqualTo(2)
        assertThat(result.nextKey).isEqualTo(4)
    }

    @Test
    fun loadReturnsNullNextKeyWhenTotalCountReached() = runTest {
        val api = FakeSearchApi().apply {
            response = sampleResponse(items = List(PAGE_SIZE) { sampleRepo(it.toLong()) }, totalCount = PAGE_SIZE)
        }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isNull()
    }

    @Test
    fun loadReturnsNullNextKeyAtThousandResultCap() = runTest {
        val api = FakeSearchApi().apply {
            response = sampleResponse(items = List(PAGE_SIZE) { sampleRepo(it.toLong()) }, totalCount = 5_000)
        }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        // page 34 * 30 = 1020 >= 1000 cap
        val result = source.load(appendParams(key = 34)) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isNull()
    }

    @Test
    fun loadBlankQueryUsesPopularQueryAndStarsSort() = runTest {
        val api = FakeSearchApi().apply {
            response = sampleResponse(items = List(PAGE_SIZE) { sampleRepo(it.toLong()) }, totalCount = 5_000)
        }
        val source = SearchRepositoriesPagingSource(api, query = "")

        source.load(refreshParams())

        assertThat(api.lastQuery).isEqualTo("stars:>10000")
        assertThat(api.lastSort).isEqualTo("stars")
        assertThat(api.lastOrder).isEqualTo("desc")
        assertThat(api.lastPage).isEqualTo(1)
        assertThat(api.lastPerPage).isEqualTo(PAGE_SIZE)
    }

    @Test
    fun loadUserQuerySendsNullSort() = runTest {
        val api = FakeSearchApi().apply {
            response = sampleResponse(items = List(PAGE_SIZE) { sampleRepo(it.toLong()) }, totalCount = 5_000)
        }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        source.load(refreshParams())

        assertThat(api.lastQuery).isEqualTo("compose")
        assertThat(api.lastSort).isNull()
        assertThat(api.lastOrder).isNull()
    }

    @Test
    fun loadReturnsErrorOnIOException() = runTest {
        val api = FakeSearchApi().apply { exception = IOException("no network") }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(IOException::class.java)
    }

    @Test
    fun loadReturnsRateLimitedExceptionOn403() = runTest {
        val api = FakeSearchApi().apply { exception = httpException(403) }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(RateLimitedException::class.java)
        assertThat((result.throwable as RateLimitedException).code).isEqualTo(403)
    }

    @Test
    fun loadReturnsRateLimitedExceptionOn429() = runTest {
        val api = FakeSearchApi().apply { exception = httpException(429) }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(RateLimitedException::class.java)
        assertThat((result.throwable as RateLimitedException).code).isEqualTo(429)
    }

    @Test
    fun loadReturnsHttpExceptionErrorOn500() = runTest {
        val api = FakeSearchApi().apply { exception = httpException(500) }
        val source = SearchRepositoriesPagingSource(api, query = "compose")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(HttpException::class.java)
        assertThat(result.throwable).isNotInstanceOf(RateLimitedException::class.java)
    }

    @Test
    fun getRefreshKeyReturnsPageAdjacentToAnchor() {
        val api = FakeSearchApi()
        val source = SearchRepositoriesPagingSource(api, query = "compose")
        val page = PagingSource.LoadResult.Page(
            data = List(PAGE_SIZE) { GithubRepo(it.toLong(), "n", "o/n", "o", "url", null, 0, null) },
            prevKey = 1,
            nextKey = 3,
        )
        val state = PagingState(
            pages = listOf(page),
            anchorPosition = 5,
            config = androidx.paging.PagingConfig(pageSize = PAGE_SIZE),
            leadingPlaceholderCount = 0,
        )

        val key = source.getRefreshKey(state)

        // closest page has prevKey=1 → 1+1 = 2
        assertThat(key).isEqualTo(2)
    }

    private fun refreshParams() = PagingSource.LoadParams.Refresh<Int>(
        key = null,
        loadSize = PAGE_SIZE,
        placeholdersEnabled = false,
    )

    private fun appendParams(key: Int) = PagingSource.LoadParams.Append(
        key = key,
        loadSize = PAGE_SIZE,
        placeholdersEnabled = false,
    )

    private fun httpException(code: Int): HttpException {
        val body = "".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, body))
    }
}
