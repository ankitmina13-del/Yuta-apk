package com.yuta.ai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val YutaDarkScheme = darkColorScheme(
    primary = YutaAccentViolet,
    onPrimary = Color.White,
    secondary = YutaAccentCyan,
    onSecondary = Color.Black,
    background = YutaBackground,
    onBackground = YutaTextPrimary,
    surface = YutaSurface,
    onSurface = YutaTextPrimary,
    surfaceVariant = YutaSurfaceElevated,
    onSurfaceVariant = YutaTextSecondary,
    error = YutaError,
)

// The signature premium gradient used across headers, buttons and accents
val YutaBrandGradient = Brush.linearGradient(
    colors = listOf(YutaGradientStart, YutaGradientEnd)
)

@Composable
fun YutaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = YutaDarkScheme,
        typography = YutaTypography,
        content = content
    )
}
