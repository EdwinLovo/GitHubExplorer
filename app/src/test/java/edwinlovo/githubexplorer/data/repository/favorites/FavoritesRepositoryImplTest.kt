package edwinlovo.githubexplorer.data.repository.favorites

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.local.fakes.FakeFavoriteRepoDao
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FavoritesRepositoryImplTest {

    private val dao = FakeFavoriteRepoDao()
    private val repository = FavoritesRepositoryImpl(dao)

    @Test
    fun addFavoriteInsertsItemObservableInFlow() = runTest {
        repository.observeFavorites().test {
            assertThat(awaitItem()).isEmpty()

            repository.addFavorite(sampleFavorite(id = 1L))

            val next = awaitItem()
            assertThat(next).hasSize(1)
            assertThat(next.first().id).isEqualTo(1L)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun removeFavoriteDeletesItemById() = runTest {
        repository.addFavorite(sampleFavorite(id = 1L))
        repository.addFavorite(sampleFavorite(id = 2L))

        repository.removeFavorite(1L)

        repository.observeFavorites().test {
            val items = awaitItem()
            assertThat(items).hasSize(1)
            assertThat(items.first().id).isEqualTo(2L)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun isFavoriteReflectsCurrentState() = runTest {
        repository.isFavorite(ownerLogin = "google", name = "compose").test {
            assertThat(awaitItem()).isFalse()

            repository.addFavorite(sampleFavorite(id = 1L, ownerLogin = "google", name = "compose"))
            assertThat(awaitItem()).isTrue()

            repository.removeFavorite(1L)
            assertThat(awaitItem()).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeFavoritesSortsByStargazersDescending() = runTest {
        repository.addFavorite(sampleFavorite(id = 1L, stars = 10))
        repository.addFavorite(sampleFavorite(id = 2L, stars = 100))
        repository.addFavorite(sampleFavorite(id = 3L, stars = 50))

        repository.observeFavorites().test {
            val items = awaitItem()
            assertThat(items.map { it.id }).containsExactly(2L, 3L, 1L).inOrder()
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun sampleFavorite(
        id: Long,
        ownerLogin: String = "owner-$id",
        name: String = "repo-$id",
        stars: Int = 100,
    ): FavoriteRepo = FavoriteRepo(
        id = id,
        name = name,
        ownerLogin = ownerLogin,
        ownerAvatarUrl = "https://a.example/$id.png",
        description = null,
        stargazersCount = stars,
        language = "Kotlin",
    )
}
