package edwinlovo.githubexplorer.presentation.ux.userprofile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileEvent
import edwinlovo.githubexplorer.presentation.ux.userprofile.contracts.UserProfileUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<UserProfileUiState>
        field = MutableStateFlow(UserProfileUiState())

    fun handleEvent(event: UserProfileEvent) = Unit
}
