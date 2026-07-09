package edwinlovo.githubexplorer.presentation.ux.languagepicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import edwinlovo.githubexplorer.presentation.ux.languagepicker.components.LanguagePickerTopBar
import edwinlovo.githubexplorer.presentation.ux.languagepicker.components.LanguageRow
import edwinlovo.githubexplorer.presentation.ux.languagepicker.contracts.LanguagePickerEvent
import edwinlovo.githubexplorer.presentation.ux.languagepicker.contracts.LanguagePickerUiState
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.SearchLanguage

@Composable
fun LanguagePickerScreen(
    navController: NavController,
    viewModel: LanguagePickerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LanguagePickerScreenContent(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@Composable
private fun LanguagePickerScreenContent(
    uiState: LanguagePickerUiState,
    handleEvent: (LanguagePickerEvent) -> Unit,
) {
    GheScaffold(
        topBar = {
            LanguagePickerTopBar(
                onBackClicked = { handleEvent(LanguagePickerEvent.OnBackClicked) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            items(SearchLanguage.entries, key = { it.name }) { language ->
                LanguageRow(
                    language = language,
                    isSelected = language.queryValue == uiState.selected,
                    onClick = { handleEvent(LanguagePickerEvent.OnLanguageSelected(it)) },
                )
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun LanguagePickerScreenContentPreview() {
    GitHubExplorerPreviewContainer {
        LanguagePickerScreenContent(
            uiState = LanguagePickerUiState(selected = "kotlin"),
            handleEvent = {},
        )
    }
}
