package edwinlovo.githubexplorer.data.remote.api

import edwinlovo.githubexplorer.domain.model.response.search.GithubRepoDto
import edwinlovo.githubexplorer.domain.model.response.users.UserProfileDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<UserProfileDto>

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<GithubRepoDto>
}
