package edwinlovo.githubexplorer.presentation.ux.users.contracts

import edwinlovo.githubexplorer.domain.model.response.search.GithubUser
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING

data class UsersUiState(
    val searchQuery: String = EMPTY_STRING,
)

sealed interface UsersEvent {
    data class OnSearchQueryChanged(val query: String) : UsersEvent
    data object OnClearSearchQuery : UsersEvent
    data class OnUserClicked(val user: GithubUser) : UsersEvent
}
