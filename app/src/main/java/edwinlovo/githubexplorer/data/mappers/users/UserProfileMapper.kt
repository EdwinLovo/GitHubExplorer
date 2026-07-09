package edwinlovo.githubexplorer.data.mappers.users

import edwinlovo.githubexplorer.domain.model.response.users.UserProfile
import edwinlovo.githubexplorer.domain.model.response.users.UserProfileDto

fun UserProfileDto.toUserProfile(): UserProfile = UserProfile(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    name = name,
    bio = bio,
    company = company,
    blog = blog,
    location = location,
    followers = followers,
    following = following,
    publicRepos = publicRepos,
)
