package edwinlovo.githubexplorer.domain.model.favorites

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetails
import org.junit.Test

class FavoriteRepoMappersTest {

    @Test
    fun toFavoriteRepoCopiesOfflineDisplayFields() {
        val details = RepoDetails(
            id = 7L,
            name = "compose",
            fullName = "google/compose",
            ownerLogin = "google",
            ownerAvatarUrl = "https://a.example/g.png",
            description = "Jetpack Compose",
            stargazersCount = 999,
            forksCount = 20,
            watchersCount = 5,
            openIssuesCount = 3,
            language = "Kotlin",
            topics = listOf("android"),
            licenseName = "MIT",
            updatedAt = "2024-01-01T00:00:00Z",
            htmlUrl = "https://github.com/google/compose",
        )

        val favorite = details.toFavoriteRepo()

        assertThat(favorite.id).isEqualTo(7L)
        assertThat(favorite.name).isEqualTo("compose")
        assertThat(favorite.ownerLogin).isEqualTo("google")
        assertThat(favorite.ownerAvatarUrl).isEqualTo("https://a.example/g.png")
        assertThat(favorite.description).isEqualTo("Jetpack Compose")
        assertThat(favorite.stargazersCount).isEqualTo(999)
        assertThat(favorite.language).isEqualTo("Kotlin")
    }
}
