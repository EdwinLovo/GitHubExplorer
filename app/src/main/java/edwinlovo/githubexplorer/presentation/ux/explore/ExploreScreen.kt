package edwinlovo.githubexplorer.presentation.ux.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.search.GithubRepo
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.navigation.ObserveResult
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.ABSOLUTE_WEIGHT
import edwinlovo.githubexplorer.presentation.utils.GheIcons
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreResultsBody
import edwinlovo.githubexplorer.presentation.ux.explore.components.ExploreSearchBar
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreEvent
import edwinlovo.githubexplorer.presentation.ux.explore.contracts.ExploreUiState
import edwinlovo.githubexplorer.presentation.ux.explore.utils.previewRepos
import edwinlovo.githubexplorer.presentation.ux.languagepicker.LanguagePickerViewModel
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.SearchLanguage
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.toSearchLanguage
import kotlinx.coroutines.flow.flowOf

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val repos = viewModel.repos.collectAsLazyPagingItems()

    navController.ObserveResult<String>(LanguagePickerViewModel.LANGUAGE_RESULT_KEY) { language ->
        viewModel.handleEvent(ExploreEvent.OnLanguageFilterResult(language))
    }

    ExploreScreenContent(
        uiState = uiState,
        repos = repos,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreScreenContent(
    uiState: ExploreUiState,
    repos: LazyPagingItems<GithubRepo>,
    handleEvent: (ExploreEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val filterLanguage = uiState.languageFilter.toSearchLanguage()
    val snackbarMessage = if (filterLanguage == SearchLanguage.Any) {
        stringResource(R.string.explore_filter_cleared)
    } else {
        stringResource(R.string.explore_filter_applied, stringResource(filterLanguage.labelRes))
    }
    LaunchedEffect(uiState.showFilterSnackbar) {
        if (uiState.showFilterSnackbar) {
            handleEvent(ExploreEvent.OnFilterSnackbarShown)
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    GheScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = GheTheme.padding.sm,
                        vertical = GheTheme.padding.xxs,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ExploreSearchBar(
                    query = uiState.searchQuery,
                    onQueryChanged = { handleEvent(ExploreEvent.OnSearchQueryChanged(it)) },
                    onClearQuery = { handleEvent(ExploreEvent.OnClearSearchQuery) },
                    modifier = Modifier.weight(ABSOLUTE_WEIGHT),
                )
                IconButton(onClick = { handleEvent(ExploreEvent.OnFilterClicked) }) {
                    Icon(
                        painter = painterResource(GheIcons.Filter),
                        contentDescription = stringResource(R.string.explore_filter),
                        tint = if (filterLanguage == SearchLanguage.Any) {
                            GheTheme.colors.iconDefault
                        } else {
                            GheTheme.colors.iconPrimary
                        },
                        modifier = Modifier.size(GheTheme.iconSize.md),
                    )
                }
            }
            PullToRefreshBox(
                isRefreshing = repos.loadState.refresh is LoadState.Loading && repos.itemCount > 0,
                onRefresh = { repos.refresh() },
                modifier = Modifier.fillMaxSize(),
            ) {
                ExploreResultsBody(
                    query = uiState.searchQuery,
                    repos = repos,
                    onRepoClick = { repo -> handleEvent(ExploreEvent.OnRepoClicked(repo)) },
                )
            }
        }
    }
}

@GhePreviewScreen
@Composable
private fun ExploreScreenContentPopulatedPreview() {
    GitHubExplorerPreviewContainer {
        ExploreScreenContent(
            uiState = ExploreUiState(searchQuery = ""),
            repos = flowOf(PagingData.from(previewRepos())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun ExploreScreenContentFilteredPreview() {
    GitHubExplorerPreviewContainer {
        ExploreScreenContent(
            uiState = ExploreUiState(searchQuery = "http", languageFilter = "kotlin"),
            repos = flowOf(PagingData.from(previewRepos())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}

@GhePreviewScreen
@Composable
private fun ExploreScreenContentEmptyPreview() {
    GitHubExplorerPreviewContainer {
        ExploreScreenContent(
            uiState = ExploreUiState(searchQuery = "asdfgh"),
            repos = flowOf(PagingData.from(emptyList<GithubRepo>())).collectAsLazyPagingItems(),
            handleEvent = {},
        )
    }
}
