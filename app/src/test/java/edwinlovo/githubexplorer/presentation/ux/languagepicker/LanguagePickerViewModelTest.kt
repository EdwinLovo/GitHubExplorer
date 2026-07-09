package edwinlovo.githubexplorer.presentation.ux.languagepicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edwinlovo.githubexplorer.presentation.ui.navigation.NavigationAction
import edwinlovo.githubexplorer.presentation.ui.navigation.PopResultKeyValue
import edwinlovo.githubexplorer.presentation.ux.explore.ExploreRoute
import edwinlovo.githubexplorer.presentation.ux.languagepicker.LanguagePickerViewModel.Companion.LANGUAGE_RESULT_KEY
import edwinlovo.githubexplorer.presentation.ux.languagepicker.contracts.LanguagePickerEvent
import edwinlovo.githubexplorer.presentation.ux.languagepicker.utils.SearchLanguage
import edwinlovo.githubexplorer.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LanguagePickerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private var viewModel: LanguagePickerViewModel? = null

    @After
    fun tearDown() {
        viewModel?.viewModelScope?.cancel()
    }

    @Test
    fun initReadsSelectedLanguageFromRoute() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel(language = "kotlin")

        assertThat(vm.uiState.value.selected).isEqualTo("kotlin")
    }

    @Test
    fun onLanguageSelectedEmitsPopWithResultToExplore() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(LanguagePickerEvent.OnLanguageSelected(SearchLanguage.Kotlin))
            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.PopWithResult(
                    resultValues = listOf(PopResultKeyValue(LANGUAGE_RESULT_KEY, "kotlin")),
                    route = ExploreRoute,
                    inclusive = false,
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onLanguageSelectedAnyReturnsEmptyQueryValue() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel(language = "kotlin")

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(LanguagePickerEvent.OnLanguageSelected(SearchLanguage.Any))
            val action = awaitItem()
            assertThat(action).isEqualTo(
                NavigationAction.PopWithResult(
                    resultValues = listOf(PopResultKeyValue(LANGUAGE_RESULT_KEY, "")),
                    route = ExploreRoute,
                    inclusive = false,
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onBackClickedEmitsPopBackStack() = runTest(mainDispatcherRule.dispatcher) {
        val vm = buildViewModel()

        vm.navigationActionFlow.test {
            assertThat(awaitItem()).isNull()
            vm.handleEvent(LanguagePickerEvent.OnBackClicked)
            val action = awaitItem()
            assertThat(action).isInstanceOf(NavigationAction.PopBackStack::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun buildViewModel(language: String = ""): LanguagePickerViewModel {
        val savedStateHandle = SavedStateHandle().apply { set("language", language) }
        return LanguagePickerViewModel(savedStateHandle).also { viewModel = it }
    }
}
