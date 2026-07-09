package edwinlovo.githubexplorer.data.local.fakes

import edwinlovo.githubexplorer.data.local.FavoriteRepoDao
import edwinlovo.githubexplorer.data.local.FavoriteRepoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeFavoriteRepoDao : FavoriteRepoDao {

    private val state = MutableStateFlow<Map<Long, FavoriteRepoEntity>>(emptyMap())

    override fun observeAll(): Flow<List<FavoriteRepoEntity>> =
        state.asStateFlow().map { it.values.sortedByDescending { entity -> entity.stargazersCount } }

    override fun isFavorite(ownerLogin: String, name: String): Flow<Boolean> =
        state.asStateFlow().map { entities ->
            entities.values.any { it.ownerLogin == ownerLogin && it.name == name }
        }

    override suspend fun upsert(entity: FavoriteRepoEntity) {
        state.value = state.value + (entity.id to entity)
    }

    override suspend fun deleteById(id: Long) {
        state.value = state.value - id
    }
}
