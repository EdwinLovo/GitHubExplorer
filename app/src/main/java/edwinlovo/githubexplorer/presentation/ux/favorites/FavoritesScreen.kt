package edwinlovo.githubexplorer.presentation.ux.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.favorites.components.FavoriteSwipeItem
import edwinlovo.githubexplorer.presentation.ux.favorites.components.FavoritesEmptyState
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesEvent
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesUiState
import edwinlovo.githubexplorer.presentation.ux.favorites.utils.previewFavorites

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreenContent(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@Composable
private fun FavoritesScreenContent(
    uiState: FavoritesUiState,
    handleEvent: (FavoritesEvent) -> Unit,
) {
    GheScaffold { paddingValues ->
        Box(
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
                uiState.favorites.isEmpty() -> {
                    FavoritesEmptyState()
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.favorites, key = { it.id }) { favorite ->
                            FavoriteSwipeItem(
                                favorite = favorite,
                                onClick = { handleEvent(FavoritesEvent.OnRepoClicked(it)) },
                                onDismissed = { handleEvent(FavoritesEvent.OnFavoriteDismissed(it)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun FavoritesScreenContentPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        FavoritesScreenContent(
            uiState = FavoritesUiState(isLoading = false, favorites = previewFavorites()),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun FavoritesScreenContentEmptyPreview() {
    GitHubExplorerPreviewContainer {
        FavoritesScreenContent(
            uiState = FavoritesUiState(isLoading = false),
            handleEvent = {},
        )
    }
}
