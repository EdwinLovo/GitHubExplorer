package edwinlovo.githubexplorer.domain.repository

import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<FavoriteRepo>>
    fun isFavorite(ownerLogin: String, name: String): Flow<Boolean>
    suspend fun addFavorite(repo: FavoriteRepo)
    suspend fun removeFavorite(repoId: Long)
}
