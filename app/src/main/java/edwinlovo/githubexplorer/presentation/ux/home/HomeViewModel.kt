package edwinlovo.githubexplorer.presentation.ux.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.ux.home.contracts.HomeEvent
import edwinlovo.githubexplorer.presentation.ux.home.contracts.HomeUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<HomeUiState>
        field = MutableStateFlow(HomeUiState())

    fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnLoad -> { /* TODO */ }
        }
    }
}
