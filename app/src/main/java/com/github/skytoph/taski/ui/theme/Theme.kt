package com.github.skytoph.taski.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PurpleBright,
    onPrimary = PurpleLight,

    secondary = PurpleGray,
    onSecondary = PurpleLight,

    tertiary = PurpleDark,
    onTertiary = Color.White,

    onSurfaceVariant = Color.Black,

    primaryContainer = PurpleVeryDark,
    onPrimaryContainer = Color.White,

    secondaryContainer = PurpleDark,
    onSecondaryContainer = PurpleLight,

    error = DarkRed
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleBright,
    onPrimary = PurpleLight,

    secondary = PurpleGrey40,
    onSecondary = PurpleDark,

    tertiary = PurpleGrey40,

    primaryContainer = PurpleLight,
    onPrimaryContainer = PurpleDark,

    secondaryContainer = PurpleGray,
    onSecondaryContainer = PurpleLight,

    error = BrightDarkRed
)

@Composable
fun TaskiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}