package edwinlovo.githubexplorer.presentation.ux.userprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.ui.components.scaffold.GheScaffold
import edwinlovo.githubexplorer.presentation.ui.navigation.HandleNavigation
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreviewScreen
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileEvent
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileUiState

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserProfileScreenContent(
        uiState = uiState,
        handleEvent = viewModel::handleEvent,
    )

    HandleNavigation(viewModel, navController)
}

@Composable
private fun UserProfileScreenContent(
    uiState: UserProfileUiState,
    handleEvent: (UserProfileEvent) -> Unit,
) {
    GheScaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.user_profile_placeholder),
                style = MaterialTheme.typography.bodyLarge,
                color = GheTheme.colors.textMuted,
            )
        }
    }
}

@GhePreviewScreen
@Composable
private fun UserProfileScreenContentPreview() {
    GitHubExplorerPreviewContainer {
        UserProfileScreenContent(
            uiState = UserProfileUiState(),
            handleEvent = {},
        )
    }
}
