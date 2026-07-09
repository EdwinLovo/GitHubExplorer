package edwinlovo.githubexplorer.presentation.ui.components.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.presentation.ui.navigation.BottomNavTab
import edwinlovo.githubexplorer.presentation.ui.theme.GheTheme
import edwinlovo.githubexplorer.presentation.utils.GhePreview
import edwinlovo.githubexplorer.presentation.utils.GitHubExplorerPreviewContainer

@Composable
internal fun GheBottomNavBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = GheTheme.colors.backgroundCard,
    ) {
        BottomNavTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = stringResource(tab.labelRes),
                        modifier = Modifier.size(GheTheme.iconSize.md),
                    )
                },
                label = { Text(text = stringResource(tab.labelRes)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GheTheme.colors.iconPrimary,
                    selectedTextColor = GheTheme.colors.textPrimary,
                    unselectedIconColor = GheTheme.colors.iconMuted,
                    unselectedTextColor = GheTheme.colors.textMuted,
                    indicatorColor = GheTheme.colors.backgroundPrimaryLight,
                ),
            )
        }
    }
}

@GhePreview
@Composable
private fun GheBottomNavBarPreview() {
    GitHubExplorerPreviewContainer {
        GheBottomNavBar(
            selectedTab = BottomNavTab.Explore,
            onTabSelected = {},
        )
    }
}
