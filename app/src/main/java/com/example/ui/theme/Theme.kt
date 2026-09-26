package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SoftGreenDarkColorScheme = darkColorScheme(
    primary = SoftGreenPrimary,
    onPrimary = SoftGreenOnPrimary,
    primaryContainer = SoftGreenPrimaryContainer,
    onPrimaryContainer = SoftGreenOnPrimaryContainer,
    secondary = SoftGreenSecondary,
    onSecondary = SoftGreenOnSecondary,
    secondaryContainer = SoftGreenSecondaryContainer,
    onSecondaryContainer = SoftGreenOnSecondaryContainer,
    tertiary = SoftGreenTertiary,
    onTertiary = SoftGreenOnTertiary,
    tertiaryContainer = SoftGreenTertiaryContainer,
    onTertiaryContainer = SoftGreenOnTertiaryContainer,
    background = SoftGreenBackground,
    onBackground = SoftGreenOnBackground,
    surface = SoftGreenSurface,
    onSurface = SoftGreenOnSurface,
    surfaceVariant = SoftGreenSurfaceVariant,
    onSurfaceVariant = SoftGreenOnSurfaceVariant,
    outline = SoftGreenOutline,
    outlineVariant = SoftGreenOutlineVariant
)

private val SoftGreenLightColorScheme = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF10B981),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA7F3D0),
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = Color(0xFF047857),
    onTertiary = Color.White,
    background = Color(0xFFF0FDF4),
    onBackground = Color(0xFF064E3B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F2937),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF4B5563),
    outline = Color(0xFF6EE7B7)
)

@Composable
fun TinSampTheme(
    darkTheme: Boolean = true, // Default to gaming dark theme with soft green
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SoftGreenDarkColorScheme else SoftGreenLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    TinSampTheme(darkTheme = darkTheme, content = content)
}
