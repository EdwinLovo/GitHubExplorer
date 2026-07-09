package edwinlovo.githubexplorer.presentation.ux.users

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersEvent
import edwinlovo.githubexplorer.presentation.ux.users.contracts.UsersUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class UsersViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<UsersUiState>
        field = MutableStateFlow(UsersUiState())

    fun handleEvent(event: UsersEvent) = Unit
}
