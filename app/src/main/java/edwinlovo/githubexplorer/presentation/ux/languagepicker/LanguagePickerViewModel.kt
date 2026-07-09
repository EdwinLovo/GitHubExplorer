package edwinlovo.githubexplorer.presentation.ux.languagepicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.PopResultKeyValue
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import edwinlovo.githubexplorer.presentation.ux.explore.ExploreRoute
import edwinlovo.githubexplorer.presentation.ux.languagepicker.contracts.LanguagePickerEvent
import edwinlovo.githubexplorer.presentation.ux.languagepicker.contracts.LanguagePickerUiState
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.SearchLanguage
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class LanguagePickerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val language: String = checkNotNull(savedStateHandle[LANGUAGE_ARG])

    val uiState: StateFlow<LanguagePickerUiState>
        field = MutableStateFlow(LanguagePickerUiState(selected = language))

    fun handleEvent(event: LanguagePickerEvent) {
        when (event) {
            is LanguagePickerEvent.OnBackClicked -> popBackStack()
            is LanguagePickerEvent.OnLanguageSelected -> returnLanguageToExplore(event.language)
        }
    }

    private fun returnLanguageToExplore(language: SearchLanguage) {
        popBackStackWithResult(
            resultValues = listOf(PopResultKeyValue(LANGUAGE_RESULT_KEY, language.queryValue)),
            route = ExploreRoute,
            inclusive = false,
        )
    }

    companion object {
        const val LANGUAGE_RESULT_KEY = "language_result"
        private const val LANGUAGE_ARG = "language"
    }
}
