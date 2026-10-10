package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TacticalDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = DeepNavy,
    primaryContainer = CommandCard,
    onPrimaryContainer = TextPrimary,
    secondary = SovereignGold,
    onSecondary = DeepNavy,
    secondaryContainer = CommandSurface,
    onSecondaryContainer = SovereignGold,
    tertiary = AlertCrimson,
    onTertiary = Color.White,
    background = DeepNavy,
    onBackground = TextPrimary,
    surface = CommandSurface,
    onSurface = TextPrimary,
    surfaceVariant = CommandCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderHighlight
)

@Composable
fun MoneyWarsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TacticalDarkColorScheme,
        typography = Typography,
        content = content
    )
}
