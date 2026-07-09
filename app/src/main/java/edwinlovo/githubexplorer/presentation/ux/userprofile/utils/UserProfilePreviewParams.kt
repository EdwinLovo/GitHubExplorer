package edwinlovo.githubexplorer.presentation.ux.userprofile.utils

import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.domain.model.response.users.UserProfile

internal fun previewUserProfile(): UserProfile = UserProfile(
    id = 1L,
    login = "torvalds",
    avatarUrl = "https://avatars.githubusercontent.com/u/1024025",
    name = "Linus Torvalds",
    bio = "Creator of Linux and Git.",
    company = "Linux Foundation",
    blog = "https://torvalds-family.blogspot.com/",
    location = "Portland, OR",
    followers = 210_432,
    following = 0,
    publicRepos = 8,
)

internal fun previewUserRepos(): List<GithubRepo> = listOf(
    GithubRepo(
        id = 1L,
        name = "linux",
        fullName = "torvalds/linux",
        ownerLogin = "torvalds",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1024025",
        description = "Linux kernel source tree.",
        stargazersCount = 178_432,
        language = "C",
    ),
    GithubRepo(
        id = 2L,
        name = "subsurface-for-dirk",
        fullName = "torvalds/subsurface-for-dirk",
        ownerLogin = "torvalds",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1024025",
        description = "Personal fork of Subsurface.",
        stargazersCount = 2_412,
        language = "C++",
    ),
)
