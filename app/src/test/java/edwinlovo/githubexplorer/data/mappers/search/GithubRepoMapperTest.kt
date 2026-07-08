package edwinlovo.githubexplorer.data.mappers.search

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepoDto
import org.junit.Test

class GithubRepoMapperTest {

    @Test
    fun toGithubRepoMapsAllFields() {
        val dto = GithubRepoDto(
            id = 42L,
            name = "compose",
            fullName = "google/compose",
            description = "Jetpack Compose",
            stargazersCount = 12_345,
            language = "Kotlin",
            owner = GithubOwnerDto(login = "google", avatarUrl = "https://a.example/google.png"),
        )

        val repo = dto.toGithubRepo()

        assertThat(repo.id).isEqualTo(42L)
        assertThat(repo.name).isEqualTo("compose")
        assertThat(repo.fullName).isEqualTo("google/compose")
        assertThat(repo.ownerLogin).isEqualTo("google")
        assertThat(repo.ownerAvatarUrl).isEqualTo("https://a.example/google.png")
        assertThat(repo.description).isEqualTo("Jetpack Compose")
        assertThat(repo.stargazersCount).isEqualTo(12_345)
        assertThat(repo.language).isEqualTo("Kotlin")
    }

    @Test
    fun toGithubRepoMapsNullDescriptionAndLanguage() {
        val dto = GithubRepoDto(
            id = 1L,
            name = "noodle",
            fullName = "u/noodle",
            description = null,
            stargazersCount = 0,
            language = null,
            owner = GithubOwnerDto(login = "u", avatarUrl = "https://a.example/u.png"),
        )

        val repo = dto.toGithubRepo()

        assertThat(repo.description).isNull()
        assertThat(repo.language).isNull()
    }
}
