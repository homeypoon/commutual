package com.example.commutual.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColors = lightColorScheme(
    primary = light_primary,
    onPrimary = light_onPrimary,
    secondary = light_secondary,
    onSecondary = light_on_secondary,
    primaryContainer = light_primaryContainer,
    onPrimaryContainer = light_onPrimaryContainer,
    tertiary = light_tertiary,
    onTertiary = light_on_tertiary,
    surface = light_surface,
    onSurface = light_on_surface,
    )

private val DarkColors = darkColorScheme(
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onPrimaryContainer,
    surface = dark_surface,
    onSurface = dark_on_surface,
)

@Composable
fun CommutualTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorSchemeColors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        typography = Typography,
        shapes = Shapes,
        content = content,
        colorScheme = colorSchemeColors
    )
}