package edwinlovo.githubexplorer.presentation.ui.theme.colors

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = compositionLocalOf<ExtendedColors> {
    error("No ExtendedColors provided — is your content wrapped in the GheTheme composable?")
}

interface ExtendedColors {
    // Background — surfaces the UI paints on
    val backgroundDefault: Color
    val backgroundMuted: Color
    val backgroundCard: Color

    // Background — Primary (brand)
    val backgroundPrimaryDefault: Color
    val backgroundPrimaryDefaultHover: Color
    val backgroundPrimaryLight: Color
    val backgroundPrimaryLightHover: Color

    // Background — Destructive (errors, delete confirmations)
    val backgroundDestructiveDefault: Color
    val backgroundDestructiveDefaultHover: Color

    // Text
    val textDefault: Color
    val textMuted: Color
    val textPrimary: Color
    val textDestructive: Color

    // Border
    val borderDefault: Color
    val borderMuted: Color
    val borderPrimary: Color

    // Icon
    val iconDefault: Color
    val iconMuted: Color
    val iconPrimary: Color

    // Transparent — for explicit "no color" cases
    val transparent: Color
}

// Light implementation
internal val lightAppColors: ExtendedColors = object : ExtendedColors {
    override val backgroundDefault = PrimitiveColors.white
    override val backgroundMuted = PrimitiveColors.slate50
    override val backgroundCard = PrimitiveColors.white

    override val backgroundPrimaryDefault = PrimitiveColors.primary500
    override val backgroundPrimaryDefaultHover = PrimitiveColors.primary700
    override val backgroundPrimaryLight = PrimitiveColors.primary50
    override val backgroundPrimaryLightHover = PrimitiveColors.primary50 // adjust to a slightly darker step

    override val backgroundDestructiveDefault = PrimitiveColors.red500
    override val backgroundDestructiveDefaultHover = PrimitiveColors.red500 // adjust to a red700

    override val textDefault = PrimitiveColors.slate900
    override val textMuted = PrimitiveColors.slate500
    override val textPrimary = PrimitiveColors.primary700
    override val textDestructive = PrimitiveColors.red500

    override val borderDefault = PrimitiveColors.slate100
    override val borderMuted = PrimitiveColors.slate50
    override val borderPrimary = PrimitiveColors.primary500

    override val iconDefault = PrimitiveColors.slate900
    override val iconMuted = PrimitiveColors.slate500
    override val iconPrimary = PrimitiveColors.primary500

    override val transparent = PrimitiveColors.transparent
}

// Dark implementation — mirror with inverted primitives
internal val darkAppColors: ExtendedColors = object : ExtendedColors {
    override val backgroundDefault = PrimitiveColors.slate950
    override val backgroundMuted = PrimitiveColors.slate900
    override val backgroundCard = PrimitiveColors.slate900

    override val backgroundPrimaryDefault = PrimitiveColors.primary500
    override val backgroundPrimaryDefaultHover = PrimitiveColors.primary700
    override val backgroundPrimaryLight = PrimitiveColors.slate900
    override val backgroundPrimaryLightHover = PrimitiveColors.slate900

    override val backgroundDestructiveDefault = PrimitiveColors.red500
    override val backgroundDestructiveDefaultHover = PrimitiveColors.red500

    override val textDefault = PrimitiveColors.white
    override val textMuted = PrimitiveColors.slate500
    override val textPrimary = PrimitiveColors.primary500
    override val textDestructive = PrimitiveColors.red500

    override val borderDefault = PrimitiveColors.slate900
    override val borderMuted = PrimitiveColors.slate900
    override val borderPrimary = PrimitiveColors.primary500

    override val iconDefault = PrimitiveColors.white
    override val iconMuted = PrimitiveColors.slate500
    override val iconPrimary = PrimitiveColors.primary500

    override val transparent = PrimitiveColors.transparent
}
