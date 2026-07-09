package edwinlovo.githubexplorer.data.repository.favorites

import edwinlovo.githubexplorer.data.local.FavoriteRepoDao
import edwinlovo.githubexplorer.data.mappers.favorites.toEntity
import edwinlovo.githubexplorer.data.mappers.favorites.toFavoriteRepo
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import edwinlovo.githubexplorer.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val dao: FavoriteRepoDao,
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<FavoriteRepo>> =
        dao.observeAll().map { entities -> entities.map { it.toFavoriteRepo() } }

    override fun isFavorite(ownerLogin: String, name: String): Flow<Boolean> =
        dao.isFavorite(ownerLogin, name)

    override suspend fun addFavorite(repo: FavoriteRepo) {
        dao.upsert(repo.toEntity())
    }

    override suspend fun removeFavorite(repoId: Long) {
        dao.deleteById(repoId)
    }
}
