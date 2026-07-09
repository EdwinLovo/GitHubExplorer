package edwinlovo.githubexplorer.data.remote.api

import edwinlovo.githubexplorer.domain.model.response.search.SearchRepositoriesResponse
import edwinlovo.githubexplorer.domain.model.response.search.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): SearchRepositoriesResponse

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): SearchUsersResponse
}
