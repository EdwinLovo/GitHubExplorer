package edwinlovo.githubexplorer.domain.model.response.search

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.domain.model.favorites.FavoriteRepo
import org.junit.Test

class GithubRepoMappersTest {

    @Test
    fun toGithubRepoMapsAllFields() {
        val favorite = FavoriteRepo(
            id = 42L,
            name = "kotlin",
            ownerLogin = "JetBrains",
            ownerAvatarUrl = "https://a.example/jetbrains.png",
            description = "The Kotlin Programming Language.",
            stargazersCount = 47_512,
            language = "Kotlin",
        )

        val repo = favorite.toGithubRepo()

        assertThat(repo.id).isEqualTo(42L)
        assertThat(repo.name).isEqualTo("kotlin")
        assertThat(repo.fullName).isEqualTo("JetBrains/kotlin")
        assertThat(repo.ownerLogin).isEqualTo("JetBrains")
        assertThat(repo.ownerAvatarUrl).isEqualTo("https://a.example/jetbrains.png")
        assertThat(repo.description).isEqualTo("The Kotlin Programming Language.")
        assertThat(repo.stargazersCount).isEqualTo(47_512)
        assertThat(repo.language).isEqualTo("Kotlin")
    }

    @Test
    fun toGithubRepoMapsNullOptionalFields() {
        val favorite = FavoriteRepo(
            id = 1L,
            name = "repo",
            ownerLogin = "owner",
            ownerAvatarUrl = "url",
            description = null,
            stargazersCount = 0,
            language = null,
        )

        val repo = favorite.toGithubRepo()

        assertThat(repo.description).isNull()
        assertThat(repo.language).isNull()
    }
}
