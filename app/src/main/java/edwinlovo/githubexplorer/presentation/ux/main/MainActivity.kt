package edwinlovo.githubexplorer.presentation.ux.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.ux.explore.ExploreRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GheTheme {
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    viewModel = viewModel,
                    startDestination = ExploreRoute,
                )
            }
        }
    }
}
