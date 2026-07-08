package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingSource
import edwinlovo.githubexplorer.domain.utils.RateLimitedException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

private const val HTTP_FORBIDDEN = 403
private const val HTTP_TOO_MANY_REQUESTS = 429

abstract class BasePagingSource<K : Any, V : Any> : PagingSource<K, V>() {
    protected suspend fun safeLoad(block: suspend () -> LoadResult<K, V>): LoadResult<K, V> =
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Timber.e(e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e)
            val translated = if (e.code() == HTTP_FORBIDDEN || e.code() == HTTP_TOO_MANY_REQUESTS) {
                RateLimitedException(e.code())
            } else {
                e
            }
            LoadResult.Error(translated)
        }
}
