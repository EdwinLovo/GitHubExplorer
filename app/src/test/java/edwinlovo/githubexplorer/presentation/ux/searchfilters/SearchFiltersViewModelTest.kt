package edwinlovo.githubexplorer.presentation.ux.searchfilters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.ux.languagepicker.LanguagePickerRoute
import edwinlovo.githubexplorer.presentation.ux.searchfilters.contracts.SearchFiltersEvent
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFiltersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private var viewModel: SearchFiltersViewModel? = null

    @After
    fun tearDown() {
        viewModel?.viewModelScope?.cancel()
    }

    @Test
    fun initReadsLanguageFromRoute() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel(language = "kotlin")

        assertThat(vm.uiState.value.language).isEqualTo("kotlin")
    }

    @Test
    fun onLanguageRowClickedEmitsNavigateToLanguagePicker() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel(language = "kotlin")

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(SearchFiltersEvent.OnLanguageRowClicked)
            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.Navigate(LanguagePickerRoute(language = "kotlin"))
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onBackClickedEmitsPopBackStack() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(SearchFiltersEvent.OnBackClicked)
            val action = awaitItem()
            assertThat(action).isInstanceOf(NavigationAction.PopBackStack::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun buildViewModel(language: String = ""): SearchFiltersViewModel {
        val savedStateHandle = SavedStateHandle().apply { set("language", language) }
        return SearchFiltersViewModel(savedStateHandle).also { viewModel = it }
    }
}
