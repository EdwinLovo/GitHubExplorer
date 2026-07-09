package edwinlovo.githubexplorer.data.remote.api

import edwinlovo.githubexplorer.domain.model.response.repos.RepoDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoApi {
    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): Response<RepoDetailsDto>
}
