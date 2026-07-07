package edwinlovo.githubexplorer.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import edwinlovo.githubexplorer.presentation.ui.theme.colors.DarkMaterialColorScheme
import edwinlovo.githubexplorer.presentation.ui.theme.colors.ExtendedColors
import edwinlovo.githubexplorer.presentation.ui.theme.colors.LightMaterialColorScheme
import edwinlovo.githubexplorer.presentation.ui.theme.colors.LocalExtendedColors
import edwinlovo.githubexplorer.presentation.ui.theme.colors.darkAppColors
import edwinlovo.githubexplorer.presentation.ui.theme.colors.lightAppColors

@Composable
fun GheTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val extendedColors = if (darkTheme) darkAppColors else lightAppColors
    val materialColorScheme = if (darkTheme) DarkMaterialColorScheme else LightMaterialColorScheme

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content,
        )
    }
}

object GheTheme {
    val colors: ExtendedColors
        @Composable get() = LocalExtendedColors.current

    val spacing get() = Dimens.Spacing
    val padding get() = Dimens.Padding
    val cornerRadius get() = Dimens.CornerRadius
    val iconSize get() = Dimens.IconSize
    val borderWidth get() = Dimens.BorderWidth
}
