package com.codewithmahad.derivativecalculator.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import com.codewithmahad.derivativecalculator.ThemeOption

// This DarkColorScheme is correct and uses our custom colors.
private val DarkColorScheme = darkColorScheme(
    primary = Muted_Blue,
    secondary = Dark_Slate,
    background = Dark_Charcoal,
    surface = Dark_Slate,
    onPrimary = Color.White,
    onSecondary = Off_White,
    onBackground = Off_White,
    onSurface = Off_White
)

private val LightColorScheme = lightColorScheme(
    primary = Steel_Blue,
    secondary = Slate_Gray,
    background = White_Smoke,
    surface = Light_Blue,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun DerivativeCalculatorTheme(
    themeOption: ThemeOption = ThemeOption.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when(themeOption) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK -> true
        ThemeOption.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // We'll set the status bar color dynamically to match the surface
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    // --- FONT SCALING FIX ---
    val typography = MaterialTheme.typography
    val density = LocalDensity.current
    val newTypography = typography.copy(
        displayLarge = typography.displayLarge.copy(fontSize = typography.displayLarge.fontSize / density.fontScale),
        displayMedium = typography.displayMedium.copy(fontSize = typography.displayMedium.fontSize / density.fontScale),
        displaySmall = typography.displaySmall.copy(fontSize = typography.displaySmall.fontSize / density.fontScale),
        headlineLarge = typography.headlineLarge.copy(fontSize = typography.headlineLarge.fontSize / density.fontScale),
        headlineMedium = typography.headlineMedium.copy(fontSize = typography.headlineMedium.fontSize / density.fontScale),
        headlineSmall = typography.headlineSmall.copy(fontSize = typography.headlineSmall.fontSize / density.fontScale),
        titleLarge = typography.titleLarge.copy(fontSize = typography.titleLarge.fontSize / density.fontScale),
        titleMedium = typography.titleMedium.copy(fontSize = typography.titleMedium.fontSize / density.fontScale),
        titleSmall = typography.titleSmall.copy(fontSize = typography.titleSmall.fontSize / density.fontScale),
        bodyLarge = typography.bodyLarge.copy(fontSize = typography.bodyLarge.fontSize / density.fontScale),
        bodyMedium = typography.bodyMedium.copy(fontSize = typography.bodyMedium.fontSize / density.fontScale),
        bodySmall = typography.bodySmall.copy(fontSize = typography.bodySmall.fontSize / density.fontScale),
        labelLarge = typography.labelLarge.copy(fontSize = typography.labelLarge.fontSize / density.fontScale),
        labelMedium = typography.labelMedium.copy(fontSize = typography.labelMedium.fontSize / density.fontScale),
        labelSmall = typography.labelSmall.copy(fontSize = typography.labelSmall.fontSize / density.fontScale)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = newTypography, // Use the new non-scalable typography
        content = content
    )
}