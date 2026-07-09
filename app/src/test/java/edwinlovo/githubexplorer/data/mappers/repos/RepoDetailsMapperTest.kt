package edwinlovo.githubexplorer.data.mappers.repos

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.domain.model.response.repos.LicenseDto
import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetailsDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import org.junit.Test

class RepoDetailsMapperTest {

    @Test
    fun toRepoDetailsMapsAllFields() {
        val dto = RepoDetailsDto(
            id = 42L,
            name = "compose",
            fullName = "google/compose",
            description = "Jetpack Compose",
            stargazersCount = 12_345,
            forksCount = 678,
            subscribersCount = 90,
            openIssuesCount = 12,
            language = "Kotlin",
            topics = listOf("android", "compose"),
            license = LicenseDto(name = "Apache License 2.0"),
            updatedAt = "2024-01-02T03:04:05Z",
            htmlUrl = "https://github.com/google/compose",
            owner = GithubOwnerDto(login = "google", avatarUrl = "https://a.example/google.png"),
        )

        val repo = dto.toRepoDetails()

        assertThat(repo.id).isEqualTo(42L)
        assertThat(repo.name).isEqualTo("compose")
        assertThat(repo.fullName).isEqualTo("google/compose")
        assertThat(repo.ownerLogin).isEqualTo("google")
        assertThat(repo.ownerAvatarUrl).isEqualTo("https://a.example/google.png")
        assertThat(repo.description).isEqualTo("Jetpack Compose")
        assertThat(repo.stargazersCount).isEqualTo(12_345)
        assertThat(repo.forksCount).isEqualTo(678)
        assertThat(repo.watchersCount).isEqualTo(90)
        assertThat(repo.openIssuesCount).isEqualTo(12)
        assertThat(repo.language).isEqualTo("Kotlin")
        assertThat(repo.topics).containsExactly("android", "compose").inOrder()
        assertThat(repo.licenseName).isEqualTo("Apache License 2.0")
        assertThat(repo.updatedAt).isEqualTo("2024-01-02T03:04:05Z")
        assertThat(repo.htmlUrl).isEqualTo("https://github.com/google/compose")
    }

    @Test
    fun toRepoDetailsUsesSubscribersCountAsWatchers() {
        val dto = sampleDto().copy(stargazersCount = 500, subscribersCount = 25)

        val repo = dto.toRepoDetails()

        assertThat(repo.stargazersCount).isEqualTo(500)
        assertThat(repo.watchersCount).isEqualTo(25)
    }

    @Test
    fun toRepoDetailsMapsNullOptionalFields() {
        val dto = sampleDto().copy(description = null, language = null, license = null, topics = emptyList())

        val repo = dto.toRepoDetails()

        assertThat(repo.description).isNull()
        assertThat(repo.language).isNull()
        assertThat(repo.licenseName).isNull()
        assertThat(repo.topics).isEmpty()
    }

    private fun sampleDto(): RepoDetailsDto = RepoDetailsDto(
        id = 1L,
        name = "repo",
        fullName = "o/repo",
        description = "d",
        stargazersCount = 1,
        forksCount = 1,
        subscribersCount = 1,
        openIssuesCount = 1,
        language = "Kotlin",
        topics = listOf("a"),
        license = LicenseDto(name = "MIT"),
        updatedAt = "2024-01-01T00:00:00Z",
        htmlUrl = "https://example",
        owner = GithubOwnerDto(login = "o", avatarUrl = "https://a"),
    )
}
