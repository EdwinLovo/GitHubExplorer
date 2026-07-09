package edwinlovo.githubexplorer.data.repository.repos

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.remote.fakes.FakeRepoApi
import edwinlovo.githubexplorer.data.remote.fakes.FakeRepoApi.Companion.errorResponse
import edwinlovo.githubexplorer.data.remote.fakes.FakeRepoApi.Companion.sampleDto
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import edwinlovo.githubexplorer.domain.utils.ApiError
import edwinlovo.githubexplorer.domain.utils.ApiSuccess
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class RepoRepositoryImplTest {

    @Test
    fun getRepositoryReturnsApiSuccessOn200() = runTest {
        val api = FakeRepoApi().apply { response = Response.success(sampleDto(name = "compose")) }
        val repository = RepoRepositoryImpl(api)

        val result = repository.getRepository("owner", "compose")

        assertThat(result).isInstanceOf(ApiSuccess::class.java)
        assertThat(api.lastOwner).isEqualTo("owner")
        assertThat(api.lastRepo).isEqualTo("compose")
        val data = (result as ApiSuccess<RepoDetails>).data
        assertThat(data.name).isEqualTo("compose")
        assertThat(data.watchersCount).isEqualTo(5)
    }

    @Test
    fun getRepositoryReturnsApiErrorWith403OnRateLimit() = runTest {
        val api = FakeRepoApi().apply { response = errorResponse(403) }
        val repository = RepoRepositoryImpl(api)

        val result = repository.getRepository("owner", "repo")

        assertThat(result).isInstanceOf(ApiError::class.java)
        assertThat((result as ApiError<RepoDetails>).code).isEqualTo(403)
    }

    @Test
    fun getRepositoryReturnsApiErrorWith429OnRateLimit() = runTest {
        val api = FakeRepoApi().apply { response = errorResponse(429) }
        val repository = RepoRepositoryImpl(api)

        val result = repository.getRepository("owner", "repo")

        assertThat(result).isInstanceOf(ApiError::class.java)
        assertThat((result as ApiError<RepoDetails>).code).isEqualTo(429)
    }

    @Test
    fun getRepositoryReturnsApiErrorWithoutCodeOnIOException() = runTest {
        val api = FakeRepoApi().apply { exception = IOException("no network") }
        val repository = RepoRepositoryImpl(api)

        val result = repository.getRepository("owner", "repo")

        assertThat(result).isInstanceOf(ApiError::class.java)
        assertThat((result as ApiError<RepoDetails>).code).isNull()
    }
}
