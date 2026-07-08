package edwinlovo.githubexplorer.data.remote.fakes

import edwinlovo.githubexplorer.data.remote.api.SearchApi
import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepoDto
import edwinlovo.githubexplorer.domain.model.response.search.SearchRepositoriesResponse

class FakeSearchApi : SearchApi {
    var response: SearchRepositoriesResponse = SearchRepositoriesResponse(
        totalCount = 0,
        incompleteResults = false,
        items = emptyList(),
    )
    var exception: Exception? = null

    var lastQuery: String? = null
        private set
    var lastSort: String? = null
        private set
    var lastOrder: String? = null
        private set
    var lastPage: Int? = null
        private set
    var lastPerPage: Int? = null
        private set

    override suspend fun searchRepositories(
        query: String,
        sort: String?,
        order: String?,
        page: Int,
        perPage: Int,
    ): SearchRepositoriesResponse {
        lastQuery = query
        lastSort = sort
        lastOrder = order
        lastPage = page
        lastPerPage = perPage
        exception?.let { throw it }
        return response
    }

    companion object {
        fun sampleRepo(id: Long, name: String = "repo-$id"): GithubRepoDto = GithubRepoDto(
            id = id,
            name = name,
            fullName = "owner/$name",
            description = "desc for $name",
            stargazersCount = 100,
            language = "Kotlin",
            owner = GithubOwnerDto(login = "owner-$id", avatarUrl = "https://a.example/$id.png"),
        )

        fun sampleResponse(items: List<GithubRepoDto>, totalCount: Int = items.size) =
            SearchRepositoriesResponse(
                totalCount = totalCount,
                incompleteResults = false,
                items = items,
            )
    }
}
