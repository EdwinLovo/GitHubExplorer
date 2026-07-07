package edwinlovo.githubexplorer.presentation.ui.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavController

sealed interface NavigationAction {
    data class Navigate(private val route: NavigationRoute) : NavigationAction {
        fun invoke(navController: NavController, resetNavigate: (NavigationAction) -> Unit) {
            navController.navigate(route)
            resetNavigate(this)
        }
    }

    data class NavigateAndPop(
        private val route: NavigationRoute,
        private val popUpToRoute: NavigationRoute,
        private val inclusive: Boolean,
    ) : NavigationAction {
        fun invoke(navController: NavController, resetNavigate: (NavigationAction) -> Unit) {
            navController.navigate(route) {
                popUpTo(this@NavigateAndPop.popUpToRoute) { inclusive = this@NavigateAndPop.inclusive }
            }
            resetNavigate(this)
        }
    }

    data class PopBackStack(
        private val route: NavigationRoute?,
        private val inclusive: Boolean,
    ) : NavigationAction {
        fun invoke(navController: NavController, resetNavigate: (NavigationAction) -> Unit) {
            if (route != null) navController.popBackStack(route = route, inclusive = inclusive)
            else navController.popBackStack()
            resetNavigate(this)
        }
    }

    data class PopWithResult(
        private val resultValues: List<PopResultKeyValue>,
        private val route: NavigationRoute? = null,
        private val inclusive: Boolean = false,
        private val saveState: Boolean = false,
    ) : NavigationAction {
        fun invoke(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            val destination = if (route != null) navController.getBackStackEntry(route)
                              else navController.previousBackStackEntry
            resultValues.forEach { destination?.savedStateHandle?.set(it.key, it.value) }
            val popped = if (route == null) navController.popBackStack()
                         else navController.popBackStack(route, inclusive = inclusive, saveState = saveState)
            resetNavigate(this)
            return popped
        }
    }

    data class PopWithResultUsingRouteName(
        private val resultValues: List<PopResultKeyValue>,
        private val routeClassName: String? = null,
        private val inclusive: Boolean = false,
        private val saveState: Boolean = false,
    ) : NavigationAction {
        @SuppressLint("RestrictedApi")
        fun invoke(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            val destination = if (routeClassName != null) {
                navController.currentBackStack.value.lastOrNull { entry ->
                    entry.destination.route?.contains(routeClassName) == true
                }
            } else navController.previousBackStackEntry
            resultValues.forEach { destination?.savedStateHandle?.set(it.key, it.value) }
            val popped = if (routeClassName != null && destination != null) {
                navController.popBackStack(destination.destination.id, inclusive = inclusive, saveState = saveState)
            } else navController.popBackStack()
            resetNavigate(this)
            return popped
        }
    }

    data class PopBackStackAndNavigate(
        private val route: NavigationRoute,
        private val inclusive: Boolean = true,
    ) : NavigationAction {
        fun invoke(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            navController.navigate(route) { popUpTo(0) { inclusive = this@PopBackStackAndNavigate.inclusive } }
            resetNavigate(this)
            return false
        }
    }
}

data class PopResultKeyValue(val key: String, val value: Any)

internal fun NavigationAction.navigate(
    navController: NavController,
    resetNavigate: (NavigationAction) -> Unit,
) {
    when (this) {
        is NavigationAction.Navigate -> invoke(navController, resetNavigate)
        is NavigationAction.NavigateAndPop -> invoke(navController, resetNavigate)
        is NavigationAction.PopBackStack -> invoke(navController, resetNavigate)
        is NavigationAction.PopBackStackAndNavigate -> invoke(navController, resetNavigate)
        is NavigationAction.PopWithResult -> invoke(navController, resetNavigate)
        is NavigationAction.PopWithResultUsingRouteName -> invoke(navController, resetNavigate)
    }
}
