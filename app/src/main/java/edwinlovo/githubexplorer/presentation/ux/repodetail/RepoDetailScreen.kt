package edwinlovo.githubexplorer.presentation.ux.repodetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.repodetail.components.RepoDetailContentBody
import edwinlovo.githubexplorer.presentation.ux.repodetail.components.RepoDetailErrorState
import edwinlovo.githubexplorer.presentation.ux.repodetail.components.RepoDetailTopBar
import edwinlovo.githubexplorer.presentation.ux.repodetail.contracts.RepoDetailEvent
import edwinlovo.githubexplorer.presentation.ux.repodetail.contracts.RepoDetailUiState
import edwinlovo.githubexplorer.presentation.ux.repodetail.utils.previewRepoDetails

@Composable
fun RepoDetailScreen(
    navController: NavController,
    viewModel: RepoDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    RepoDetailScreenContent(
        uiState = uiState,
        handleEvent = { event ->
            if (event is RepoDetailEvent.OnOpenInBrowserClicked) {
                uriHandler.openUri(event.url)
            }
            viewModel.handleEvent(event)
        },
    )

    HandleNavigation(viewModel, navController)
}

@Composable
private fun RepoDetailScreenContent(
    uiState: RepoDetailUiState,
    handleEvent: (RepoDetailEvent) -> Unit,
) {
    val repo = uiState.repo
    GheScaffold(
        topBar = {
            RepoDetailTopBar(
                title = repo?.fullName.orEmpty(),
                isFavorite = uiState.isFavorite,
                canFavorite = repo != null,
                canOpenInBrowser = repo != null,
                onBackClicked = { handleEvent(RepoDetailEvent.OnBackClicked) },
                onFavoriteToggled = { handleEvent(RepoDetailEvent.OnFavoriteToggled) },
                onOpenInBrowserClicked = {
                    repo?.htmlUrl?.let { url ->
                        handleEvent(RepoDetailEvent.OnOpenInBrowserClicked(url))
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.hasError || repo == null -> {
                    RepoDetailErrorState(
                        isRateLimited = uiState.isRateLimited,
                        onRetry = { handleEvent(RepoDetailEvent.OnRetryClicked) },
                    )
                }
                else -> {
                    RepoDetailContentBody(
                        repo = repo,
                        onOwnerClicked = { handleEvent(RepoDetailEvent.OnOwnerClicked) },
                    )
                }
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun RepoDetailScreenContentLoadedPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailScreenContent(
            uiState = RepoDetailUiState(isLoading = false, repo = previewRepoDetails()),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun RepoDetailScreenContentLoadedFavoritedPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailScreenContent(
            uiState = RepoDetailUiState(isLoading = false, repo = previewRepoDetails(), isFavorite = true),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun RepoDetailScreenContentLoadingPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailScreenContent(
            uiState = RepoDetailUiState(isLoading = true),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun RepoDetailScreenContentErrorPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailScreenContent(
            uiState = RepoDetailUiState(isLoading = false, hasError = true, isRateLimited = false),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun RepoDetailScreenContentRateLimitedPreview() {
    GitHubExplorerPreviewContainer {
        RepoDetailScreenContent(
            uiState = RepoDetailUiState(isLoading = false, hasError = true, isRateLimited = true),
            handleEvent = {},
        )
    }
}
