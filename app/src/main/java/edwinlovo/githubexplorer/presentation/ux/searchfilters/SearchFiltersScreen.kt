package edwinlovo.githubexplorer.presentation.ux.searchfilters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.searchfilters.components.FilterLanguageRow
import edwinlovo.githubexplorer.presentation.ux.searchfilters.components.SearchFiltersTopBar
import edwinlovo.githubexplorer.presentation.ux.searchfilters.contracts.SearchFiltersEvent
import edwinlovo.githubexplorer.presentation.ux.searchfilters.contracts.SearchFiltersUiState

@Composable
fun SearchFiltersScreen(
    navController: NavController,
    viewModel: SearchFiltersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchFiltersScreenContent(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@Composable
private fun SearchFiltersScreenContent(
    uiState: SearchFiltersUiState,
    handleEvent: (SearchFiltersEvent) -> Unit,
) {
    GheScaffold(
        topBar = {
            SearchFiltersTopBar(
                onBackClicked = { handleEvent(SearchFiltersEvent.OnBackClicked) },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            FilterLanguageRow(
                language = uiState.language,
                onClick = { handleEvent(SearchFiltersEvent.OnLanguageRowClicked) },
            )
        }
    }
}

@GhePreviewScreen
@Composable
private fun SearchFiltersScreenContentPreview() {
    GitHubExplorerPreviewContainer {
        SearchFiltersScreenContent(
            uiState = SearchFiltersUiState(language = "kotlin"),
            handleEvent = {},
        )
    }
}
