package edwinlovo.githubexplorer.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ViewModelNav {
    val navigationActionFlow: StateFlow<NavigationAction?>

    fun navigate(route: NavigationRoute)
    fun navigateToTab(route: NavigationRoute)
    fun navigateAndPop(route: NavigationRoute, popUpToRoute: NavigationRoute, inclusive: Boolean)
    fun resetNavigate(action: NavigationAction)
    fun popBackStack(route: NavigationRoute? = null, inclusive: Boolean = false)
    fun popBackStackWithResult(
        resultValues: List<PopResultKeyValue>,
        route: NavigationRoute? = null,
        inclusive: Boolean = false,
    )
    fun popBackStackWithResultUsingRouteName(
        resultValues: List<PopResultKeyValue>,
        routeClassName: String?,
        inclusive: Boolean,
    )
    fun popBackStackAndNavigate(route: NavigationRoute, inclusive: Boolean = true)
}

internal class ViewModelNavImpl : ViewModelNav {
    private val _navigationActionFlow = MutableStateFlow<NavigationAction?>(null)
    override val navigationActionFlow = _navigationActionFlow.asStateFlow()

    override fun navigate(route: NavigationRoute) {
        _navigationActionFlow.compareAndSet(null, NavigationAction.Navigate(route))
    }
    override fun navigateToTab(route: NavigationRoute) {
        _navigationActionFlow.compareAndSet(null, NavigationAction.NavigateToTab(route))
    }
    override fun navigateAndPop(route: NavigationRoute, popUpToRoute: NavigationRoute, inclusive: Boolean) {
        _navigationActionFlow.compareAndSet(null, NavigationAction.NavigateAndPop(route, popUpToRoute, inclusive))
    }
    override fun resetNavigate(action: NavigationAction) {
        _navigationActionFlow.compareAndSet(action, null)
    }
    override fun popBackStack(route: NavigationRoute?, inclusive: Boolean) {
        _navigationActionFlow.compareAndSet(null, NavigationAction.PopBackStack(route, inclusive))
    }
    override fun popBackStackWithResult(
        resultValues: List<PopResultKeyValue>,
        route: NavigationRoute?,
        inclusive: Boolean,
    ) {
        _navigationActionFlow.compareAndSet(null, NavigationAction.PopWithResult(resultValues, route, inclusive))
    }
    override fun popBackStackWithResultUsingRouteName(
        resultValues: List<PopResultKeyValue>,
        routeClassName: String?,
        inclusive: Boolean,
    ) {
        _navigationActionFlow.compareAndSet(
            null,
            NavigationAction.PopWithResultUsingRouteName(resultValues, routeClassName, inclusive),
        )
    }
    override fun popBackStackAndNavigate(route: NavigationRoute, inclusive: Boolean) {
        _navigationActionFlow.compareAndSet(null, NavigationAction.PopBackStackAndNavigate(route, inclusive))
    }
}

@Composable
fun HandleNavigation(viewModelNav: ViewModelNav, navController: NavController?) {
    navController ?: return
    val action by viewModelNav.navigationActionFlow.collectAsStateWithLifecycle()
    LaunchedEffect(action) {
        action?.navigate(navController, viewModelNav::resetNavigate)
    }
}

@Composable
fun NavController.ObserveBooleanResult(key: String, onResult: () -> Unit) {
    val backStackEntry by currentBackStackEntryAsState()
    val result = backStackEntry?.savedStateHandle?.getStateFlow(key, false)?.collectAsStateWithLifecycle()
    LaunchedEffect(result?.value) {
        if (result?.value == true) {
            backStackEntry?.savedStateHandle?.set(key, false)   // consume once
            onResult()
        }
    }
}
