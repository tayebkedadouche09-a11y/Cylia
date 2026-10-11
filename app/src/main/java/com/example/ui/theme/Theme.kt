package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RomanticDarkColorScheme = darkColorScheme(
    primary = NeonPink,
    onPrimary = Color.White,
    primaryContainer = NeonPinkMuted,
    onPrimaryContainer = TextPrimary,
    secondary = RomanticPurple,
    onSecondary = Color.White,
    secondaryContainer = RomanticPurpleDark,
    onSecondaryContainer = TextSecondary,
    tertiary = RomanticViolet,
    onTertiary = Color.White,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    surfaceTint = NeonPink,
    outline = DarkSurfaceHigh,
    outlineVariant = Color(0xFF381A57),
    error = DangerRose,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RomanticDarkColorScheme,
        typography = Typography,
        content = content
    )
}
