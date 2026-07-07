package edwinlovo.githubexplorer.presentation.ui.theme.colors

import androidx.compose.ui.graphics.Color

internal object PrimitiveColors {
    // Slate — neutral cool grays
    val slate50 = Color(0xFFF8FAFC)
    val slate100 = Color(0xFFF1F5F9)
    val slate500 = Color(0xFF64748B)
    val slate900 = Color(0xFF0F172A)
    val slate950 = Color(0xFF020617)
    // ... slate200, slate300, slate400, slate600, slate700, slate800 as needed

    // Gray — neutral warm grays (only if distinct from slate in your palette)
    val gray50 = Color(0xFFF9FAFB)
    val gray500 = Color(0xFF6B7280)
    val gray900 = Color(0xFF111827)

    // Primary — brand color
    val primary50 = Color(0xFFEFF6FF)
    val primary500 = Color(0xFF3B82F6)
    val primary700 = Color(0xFF1D4ED8)

    // Secondary, Tertiary — additional brand roles if your design system has them
    val secondary500 = Color(0xFF14B8A6)
    val tertiary500 = Color(0xFFF59E0B)

    // Semantic — error, success, warning
    val red500 = Color(0xFFEF4444)
    val green500 = Color(0xFF22C55E)
    val amber500 = Color(0xFFF59E0B)

    // Pure
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val transparent = Color(0x00000000)
}
