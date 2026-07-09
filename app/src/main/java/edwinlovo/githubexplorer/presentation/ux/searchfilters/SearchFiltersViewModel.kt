package edwinlovo.githubexplorer.presentation.ux.searchfilters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.ux.languagepicker.LanguagePickerRoute
import edwinlovo.githubexplorer.presentation.ux.searchfilters.contracts.SearchFiltersEvent
import edwinlovo.githubexplorer.presentation.ux.searchfilters.contracts.SearchFiltersUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SearchFiltersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val language: String = checkNotNull(savedStateHandle[LANGUAGE_ARG])

    val uiState: StateFlow<SearchFiltersUiState>
        field = MutableStateFlow(SearchFiltersUiState(language = language))

    fun handleEvent(event: SearchFiltersEvent) {
        when (event) {
            is SearchFiltersEvent.OnBackClicked -> popBackStack()
            is SearchFiltersEvent.OnLanguageRowClicked -> navigateToLanguagePicker()
        }
    }

    private fun navigateToLanguagePicker() {
        navigate(LanguagePickerRoute(language = uiState.value.language))
    }

    companion object {
        private const val LANGUAGE_ARG = "language"
    }
}
