package com.example.flow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrightGreen,
    secondary = ForestGreen,
    tertiary = BrightGreen,
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2D2D2D).copy(alpha = 0.8f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = ForestGreen,
    tertiary = BrightGreen,
    background = WarmSand,
    surface = FrostedGlass.copy(alpha = 0.8f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = DarkGrey,
    onBackground = DarkGrey,
    onSurface = DarkGrey
)

@Composable
fun FlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}