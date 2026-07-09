package edwinlovo.githubexplorer.data.remote.fakes

import edwinlovo.githubexplorer.data.remote.api.SearchApi
import edwinlovo.githubexplorer.domain.model.response.search.GithubOwnerDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepoDto
import edwinlovo.githubexplorer.domain.model.response.search.GithubUserDto
import edwinlovo.githubexplorer.domain.model.response.search.SearchRepositoriesResponse
import edwinlovo.githubexplorer.domain.model.response.search.SearchUsersResponse

class FakeSearchApi : SearchApi {
    var response: SearchRepositoriesResponse = SearchRepositoriesResponse(
        totalCount = 0,
        incompleteResults = false,
        items = emptyList(),
    )
    var usersResponse: SearchUsersResponse = SearchUsersResponse(
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

    var lastUsersQuery: String? = null
        private set
    var lastUsersPage: Int? = null
        private set
    var lastUsersPerPage: Int? = null
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

    override suspend fun searchUsers(
        query: String,
        page: Int,
        perPage: Int,
    ): SearchUsersResponse {
        lastUsersQuery = query
        lastUsersPage = page
        lastUsersPerPage = perPage
        exception?.let { throw it }
        return usersResponse
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

        fun sampleUser(id: Long, login: String = "user-$id", type: String = "User"): GithubUserDto =
            GithubUserDto(
                id = id,
                login = login,
                avatarUrl = "https://a.example/$id.png",
                type = type,
            )

        fun sampleUsersResponse(items: List<GithubUserDto>, totalCount: Int = items.size) =
            SearchUsersResponse(
                totalCount = totalCount,
                incompleteResults = false,
                items = items,
            )
    }
}
