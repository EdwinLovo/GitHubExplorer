package edwinlovo.githubexplorer.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_repos")
data class FavoriteRepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "owner_login") val ownerLogin: String,
    @ColumnInfo(name = "owner_avatar_url") val ownerAvatarUrl: String,
    val description: String?,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int,
    val language: String?,
)
