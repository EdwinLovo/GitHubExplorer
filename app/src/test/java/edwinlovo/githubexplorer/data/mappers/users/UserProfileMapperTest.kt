package edwinlovo.githubexplorer.data.mappers.users

import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.domain.model.response.users.UserProfileDto
import org.junit.Test

class UserProfileMapperTest {

    @Test
    fun toUserProfileMapsAllFields() {
        val dto = UserProfileDto(
            id = 42L,
            login = "torvalds",
            avatarUrl = "https://a.example/torvalds.png",
            name = "Linus Torvalds",
            bio = "Creator of Linux",
            company = "Linux Foundation",
            blog = "https://torvalds.example",
            location = "Portland",
            followers = 210_000,
            following = 0,
            publicRepos = 8,
        )

        val user = dto.toUserProfile()

        assertThat(user.id).isEqualTo(42L)
        assertThat(user.login).isEqualTo("torvalds")
        assertThat(user.avatarUrl).isEqualTo("https://a.example/torvalds.png")
        assertThat(user.name).isEqualTo("Linus Torvalds")
        assertThat(user.bio).isEqualTo("Creator of Linux")
        assertThat(user.company).isEqualTo("Linux Foundation")
        assertThat(user.blog).isEqualTo("https://torvalds.example")
        assertThat(user.location).isEqualTo("Portland")
        assertThat(user.followers).isEqualTo(210_000)
        assertThat(user.following).isEqualTo(0)
        assertThat(user.publicRepos).isEqualTo(8)
    }

    @Test
    fun toUserProfileMapsNullOptionalFields() {
        val dto = UserProfileDto(
            id = 1L,
            login = "ghost",
            avatarUrl = "url",
            name = null,
            bio = null,
            company = null,
            blog = null,
            location = null,
            followers = 0,
            following = 0,
            publicRepos = 0,
        )

        val user = dto.toUserProfile()

        assertThat(user.name).isNull()
        assertThat(user.bio).isNull()
        assertThat(user.company).isNull()
        assertThat(user.blog).isNull()
        assertThat(user.location).isNull()
    }
}
