package edwinlovo.githubexplorer.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRepoDao {

    @Query("SELECT * FROM favorite_repos ORDER BY stargazers_count DESC")
    fun observeAll(): Flow<List<FavoriteRepoEntity>>

    @Query(
        "SELECT EXISTS(SELECT 1 FROM favorite_repos WHERE owner_login = :ownerLogin AND name = :name)"
    )
    fun isFavorite(ownerLogin: String, name: String): Flow<Boolean>

    @Upsert
    suspend fun upsert(entity: FavoriteRepoEntity)

    @Query("DELETE FROM favorite_repos WHERE id = :id")
    suspend fun deleteById(id: Long)
}
