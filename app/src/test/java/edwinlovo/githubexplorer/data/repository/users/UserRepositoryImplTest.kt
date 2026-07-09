package edwinlovo.githubexplorer.data.repository.users

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.remote.fakes.FakeUserApi
import edwinlovo.githubexplorer.data.remote.fakes.FakeUserApi.Companion.errorResponse
import edwinlovo.githubexplorer.data.remote.fakes.FakeUserApi.Companion.sampleDto
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.domain.utils.ApiError
import edwinlovo.githubexplorer.domain.utils.ApiSuccess
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class UserRepositoryImplTest {

    @Test
    fun getUserReturnsApiSuccessOn200() = runTest {
        val api = FakeUserApi().apply {
            userResponse = Response.success(sampleDto(login = "torvalds"))
        }
        val repository = UserRepositoryImpl(api)

        val result = repository.getUser("torvalds")

        assertThat(result).isInstanceOf(ApiSuccess::class.java)
        assertThat(api.lastUsername).isEqualTo("torvalds")
        val data = (result as ApiSuccess<UserProfile>).data
        assertThat(data.login).isEqualTo("torvalds")
        assertThat(data.followers).isEqualTo(100)
    }

    @Test
    fun getUserReturnsApiErrorWith403OnRateLimit() = runTest {
        val api = FakeUserApi().apply { userResponse = errorResponse(403) }
        val repository = UserRepositoryImpl(api)

        val result = repository.getUser("ghost")

        assertThat(result).isInstanceOf(ApiError::class.java)
        assertThat((result as ApiError<UserProfile>).code).isEqualTo(403)
    }

    @Test
    fun getUserReturnsApiErrorWithoutCodeOnIOException() = runTest {
        val api = FakeUserApi().apply { exception = IOException("no network") }
        val repository = UserRepositoryImpl(api)

        val result = repository.getUser("ghost")

        assertThat(result).isInstanceOf(ApiError::class.java)
        assertThat((result as ApiError<UserProfile>).code).isNull()
    }
}
