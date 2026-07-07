package edwinlovo.githubexplorer.presentation.ux.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.home.contracts.HomeEvent
import edwinlovo.githubexplorer.presentation.ux.home.contracts.HomeUiState

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    handleEvent: (HomeEvent) -> Unit,
) {
    Text(text = stringResource(R.string.home_title))
}

private class HomeUiStateProvider : PreviewParameterProvider<HomeUiState> {
    override val values = sequenceOf(HomeUiState())
}

@GhePreviewScreen
@Composable
private fun HomeContentPreview(
    @PreviewParameter(HomeUiStateProvider::class) uiState: HomeUiState,
) {
    GitHubExplorerPreviewContainer {
        HomeScreenContent(uiState = uiState, handleEvent = {})
    }
}
