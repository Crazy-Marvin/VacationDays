package rocks.poopjournal.vacationdays.presentation.widgets

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceTheme
import androidx.glance.material3.ColorProviders
import rocks.poopjournal.vacationdays.presentation.ui.theme.darkBlack

@Composable
fun WidgetTheme(
    content: @Composable () -> Unit,
) {
    GlanceTheme(
        colors =
            ColorProviders(
                light =
                    lightColorScheme(
                        background = Color.White,
                        onBackground = Color.Black,
                        onSecondary = Color(0xFF7B858E)

                    ),
                dark =
                    darkColorScheme(
                        background = darkBlack,
                        onBackground = Color.White,
                        onSecondary = Color(0xFF7B858E)
                    ),
            ),
        content = content,
    )
}