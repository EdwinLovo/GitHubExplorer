package edwinlovo.githubexplorer.data.mappers.favorites

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.data.local.FavoriteRepoEntity
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import org.junit.Test

class FavoriteRepoEntityMapperTest {

    @Test
    fun toFavoriteRepoCopiesAllFields() {
        val entity = FavoriteRepoEntity(
            id = 1L,
            name = "compose",
            ownerLogin = "google",
            ownerAvatarUrl = "https://a.example/g.png",
            description = "desc",
            stargazersCount = 42,
            language = "Kotlin",
        )

        val model = entity.toFavoriteRepo()

        assertThat(model).isEqualTo(
            FavoriteRepo(
                id = 1L,
                name = "compose",
                ownerLogin = "google",
                ownerAvatarUrl = "https://a.example/g.png",
                description = "desc",
                stargazersCount = 42,
                language = "Kotlin",
            )
        )
    }

    @Test
    fun toEntityCopiesAllFields() {
        val model = FavoriteRepo(
            id = 2L,
            name = "coil",
            ownerLogin = "coil-kt",
            ownerAvatarUrl = "https://a.example/c.png",
            description = null,
            stargazersCount = 500,
            language = null,
        )

        val entity = model.toEntity()

        assertThat(entity.id).isEqualTo(2L)
        assertThat(entity.description).isNull()
        assertThat(entity.language).isNull()
    }
}
