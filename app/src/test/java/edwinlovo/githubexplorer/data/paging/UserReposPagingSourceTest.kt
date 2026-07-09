package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.paging.UserReposPagingSource.Companion.PAGE_SIZE
import edwinlovo.githubexplorer.data.remote.fakes.FakeUserApi
import edwinlovo.githubexplorer.data.remote.fakes.FakeUserApi.Companion.sampleRepoDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserReposPagingSourceTest {

    @Test
    fun loadRefreshReturnsPageWithNullPrevAndNextKeyTwo() = runTest {
        val api = FakeUserApi().apply {
            reposResponse = List(PAGE_SIZE) { sampleRepoDto(it.toLong()) }
        }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Page

        assertThat(result.prevKey).isNull()
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.data).hasSize(PAGE_SIZE)
    }

    @Test
    fun loadAppendReturnsAdjacentPrevAndNextKeys() = runTest {
        val api = FakeUserApi().apply {
            reposResponse = List(PAGE_SIZE) { sampleRepoDto(it.toLong()) }
        }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(appendParams(key = 3)) as PagingSource.LoadResult.Page

        assertThat(result.prevKey).isEqualTo(2)
        assertThat(result.nextKey).isEqualTo(4)
    }

    @Test
    fun loadReturnsNullNextKeyOnPartialPage() = runTest {
        val api = FakeUserApi().apply {
            reposResponse = List(PAGE_SIZE - 1) { sampleRepoDto(it.toLong()) }
        }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Page

        assertThat(result.nextKey).isNull()
    }

    @Test
    fun loadPassesUsernamePageAndPerPageToApi() = runTest {
        val api = FakeUserApi().apply {
            reposResponse = List(PAGE_SIZE) { sampleRepoDto(it.toLong()) }
        }
        val source = UserReposPagingSource(api, username = "torvalds")

        source.load(refreshParams())

        assertThat(api.lastReposUsername).isEqualTo("torvalds")
        assertThat(api.lastReposPage).isEqualTo(1)
        assertThat(api.lastReposPerPage).isEqualTo(PAGE_SIZE)
    }

    @Test
    fun loadReturnsErrorOnIOException() = runTest {
        val api = FakeUserApi().apply { exception = IOException("no network") }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(IOException::class.java)
    }

    @Test
    fun loadReturnsRateLimitedExceptionOn403() = runTest {
        val api = FakeUserApi().apply { exception = httpException(403) }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(RateLimitedException::class.java)
        assertThat((result.throwable as RateLimitedException).code).isEqualTo(403)
    }

    @Test
    fun loadReturnsRateLimitedExceptionOn429() = runTest {
        val api = FakeUserApi().apply { exception = httpException(429) }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(RateLimitedException::class.java)
        assertThat((result.throwable as RateLimitedException).code).isEqualTo(429)
    }

    @Test
    fun loadReturnsHttpExceptionErrorOn500() = runTest {
        val api = FakeUserApi().apply { exception = httpException(500) }
        val source = UserReposPagingSource(api, username = "torvalds")

        val result = source.load(refreshParams()) as PagingSource.LoadResult.Error

        assertThat(result.throwable).isInstanceOf(HttpException::class.java)
        assertThat(result.throwable).isNotInstanceOf(RateLimitedException::class.java)
    }

    @Test
    fun getRefreshKeyReturnsPageAdjacentToAnchor() {
        val api = FakeUserApi()
        val source = UserReposPagingSource(api, username = "torvalds")
        val page = PagingSource.LoadResult.Page(
            data = List(PAGE_SIZE) {
                GithubRepo(it.toLong(), "n", "o/n", "o", "url", null, 0, null)
            },
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
