package edwinlovo.githubexplorer.presentation.ux.favorites

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesEvent
import edwinlovo.githubexplorer.presentation.ux.favorites.contracts.FavoritesUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState: StateFlow<FavoritesUiState>
        field = MutableStateFlow(FavoritesUiState())

    fun handleEvent(event: FavoritesEvent) = Unit
}
