package edwinlovo.githubexplorer.domain.utils

sealed interface ApiResult<T : Any>

class ApiSuccess<T : Any>(val data: T) : ApiResult<T>

class ApiError<T : Any>(val code: Int? = null, val message: String? = null) : ApiResult<T>

class ApiLoading<T : Any>(val data: T? = null) : ApiResult<T>

suspend fun <T : Any> ApiResult<T>.onSuccess(block: suspend (T) -> Unit): ApiResult<T> {
    if (this is ApiSuccess) block(data)
    return this
}

suspend fun <T : Any> ApiResult<T>.onError(block: suspend (code: Int?, message: String?) -> Unit): ApiResult<T> {
    if (this is ApiError) block(code, message)
    return this
}

suspend fun <T : Any> ApiResult<T>.onLoading(block: suspend () -> Unit): ApiResult<T> {
    if (this is ApiLoading) block()
    return this
}
