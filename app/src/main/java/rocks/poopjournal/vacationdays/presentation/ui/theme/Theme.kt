package rocks.poopjournal.vacationdays.presentation.ui.theme

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
    primary = darkPrimary,
    secondary = darkPrimary,
    background = darkBlack,
    onBackground = Color.White,
    onSurface = Color.Black,
    surface = darkOrange,
    onSecondary = darkPrimary,
    onSecondaryContainer = Color.White,
    onTertiaryContainer = darkGray,
    onTertiary = darkPrimary,
    onPrimary = Color.White,
    onPrimaryContainer = darkGray,
    outlineVariant = darkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = primary,
    secondary = lightGray,
    onBackground = Color.White,
    background = Color.White,
    onSurface = Color.White,
    surface = primary,
    onSecondary = Color.White,
    onSecondaryContainer = Color.Black,
    onTertiaryContainer = primary,
    onTertiary = lightGray,
    onPrimary = Color.Black,
    onPrimaryContainer = Color.White,
    outlineVariant = lightBorder

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MyVacationDays2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
            window.statusBarColor =  if(darkTheme) darkPrimary.toArgb() else primary.toArgb() // change color status bar here
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}