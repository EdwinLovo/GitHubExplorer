package edwinlovo.githubexplorer.data.paging

import androidx.paging.PagingSource
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

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
            LoadResult.Error(e)
        }
}
