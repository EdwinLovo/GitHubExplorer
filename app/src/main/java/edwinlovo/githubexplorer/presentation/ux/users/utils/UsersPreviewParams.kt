package edwinlovo.githubexplorer.presentation.ux.users.utils

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import edwinlovo.githubexplorer.domain.model.response.search.AccountType
import edwinlovo.githubexplorer.domain.model.response.search.GithubUser

internal fun previewUsersPagingData(refresh: LoadState): PagingData<GithubUser> =
    PagingData.empty(
        sourceLoadStates = LoadStates(
            refresh = refresh,
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true),
        ),
    )

internal fun previewUsers(): List<GithubUser> = listOf(
    GithubUser(
        id = 1L,
        login = "torvalds",
        avatarUrl = "https://avatars.githubusercontent.com/u/1024025",
        type = AccountType.USER,
    ),
    GithubUser(
        id = 2L,
        login = "google",
        avatarUrl = "https://avatars.githubusercontent.com/u/1342004",
        type = AccountType.ORGANIZATION,
    ),
    GithubUser(
        id = 3L,
        login = "JetBrains",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437",
        type = AccountType.ORGANIZATION,
    ),
)
