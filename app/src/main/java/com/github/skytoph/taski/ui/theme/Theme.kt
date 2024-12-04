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
import com.github.skytoph.taski.presentation.settings.theme.AppTheme

private val DarkColorScheme = darkColorScheme(
    background = Black,

    primary = PurpleLightBright,
    onPrimary = PurpleLight,

    secondary = PurpleBright,
    onSecondary = PurpleLight,

    tertiary = PurpleDark,
    onTertiary = Color.White,

    primaryContainer = PurpleVeryDark,
    onPrimaryContainer = Color.White,

    secondaryContainer = PurpleDark,
    onSecondaryContainer = PurpleLight,

    surface = PurpleVeryDark,
    surfaceVariant = PurpleVeryDark,

    inverseSurface = Color.White,

    tertiaryContainer = PurpleVeryDark,
    onTertiaryContainer = PurpleDark,

    error = DarkRed,
    errorContainer = GrayRed
)

private val LightColorScheme = lightColorScheme(
    background = LightGray,

    primary = PurpleLightBright,
    onPrimary = PurpleLight,

    secondary = PurpleGrey40,
    onSecondary = PurpleDark,

    tertiary = BlueLightGray,

    primaryContainer = Color.White,
    onPrimaryContainer = PurpleDark,

    secondaryContainer = BlueLight,
    onSecondaryContainer = Color.White,

    tertiaryContainer = BlueLight,
    onTertiaryContainer = PurpleLight,

    surface = Color.White,
    surfaceVariant = Color.White,

    error = BrightDarkRed
)

@Composable
fun HabitMateTheme(
    theme: AppTheme? = AppTheme.System,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isDark = theme == null || theme == AppTheme.Dark || (theme == AppTheme.System && darkTheme)
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDark -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val background = colorScheme.background.toArgb()
            window.statusBarColor = background
            window.navigationBarColor = background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}