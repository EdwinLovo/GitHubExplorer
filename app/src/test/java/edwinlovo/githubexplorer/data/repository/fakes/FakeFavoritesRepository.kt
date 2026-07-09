package edwinlovo.githubexplorer.data.repository.fakes

import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import edwinlovo.githubexplorer.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeFavoritesRepository : FavoritesRepository {

    private val state = MutableStateFlow<Map<Long, FavoriteRepo>>(emptyMap())

    private val _addCalls = mutableListOf<FavoriteRepo>()
    val addCalls: List<FavoriteRepo> get() = _addCalls.toList()

    private val _removeCalls = mutableListOf<Long>()
    val removeCalls: List<Long> get() = _removeCalls.toList()

    override fun observeFavorites(): Flow<List<FavoriteRepo>> =
        state.asStateFlow().map { it.values.toList() }

    override fun isFavorite(ownerLogin: String, name: String): Flow<Boolean> =
        state.asStateFlow().map { entries ->
            entries.values.any { it.ownerLogin == ownerLogin && it.name == name }
        }

    override suspend fun addFavorite(repo: FavoriteRepo) {
        _addCalls += repo
        state.value = state.value + (repo.id to repo)
    }

    override suspend fun removeFavorite(repoId: Long) {
        _removeCalls += repoId
        state.value = state.value - repoId
    }
}
