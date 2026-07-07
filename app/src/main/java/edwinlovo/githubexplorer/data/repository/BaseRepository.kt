package edwinlovo.githubexplorer.data.repository

import edwinlovo.githubexplorer.domain.utils.ApiError
import edwinlovo.githubexplorer.domain.utils.ApiLoading
import edwinlovo.githubexplorer.domain.utils.ApiResult
import edwinlovo.githubexplorer.domain.utils.ApiSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

open class BaseRepository {
    suspend fun <T : Any, R : Any> safeCallSuspend(
        call: suspend () -> Response<T>,
        mapper: (T) -> R,
    ): ApiResult<R> =
        try {
            val response = call()
            when {
                response.isSuccessful -> response.body()?.let { ApiSuccess(mapper(it)) }
                    ?: ApiError(code = response.code(), message = "Empty body")
                else -> ApiError(code = response.code(), message = response.errorBody()?.string())
            }
        } catch (e: IOException) {
            Timber.e(e)
            ApiError(message = e.message)
        } catch (e: HttpException) {
            Timber.e(e)
            ApiError(code = e.code(), message = e.message)
        }

    fun <T : Any, R : Any> safeCall(
        call: suspend () -> Response<T>,
        mapper: (T) -> R,
    ): Flow<ApiResult<R>> =
        flow { emit(safeCallSuspend(call, mapper)) }
            .onStart { emit(ApiLoading()) }
            .catch { throwable ->
                Timber.e(throwable)
                emit(ApiError(message = throwable.message))
            }
}
