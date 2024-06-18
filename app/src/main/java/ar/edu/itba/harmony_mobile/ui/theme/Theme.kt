package ar.edu.itba.harmony_mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = Color.White,
    secondary = secondary,
    onSecondary = primary,
    tertiary = tertiary,
    onTertiary = Color.Black,
    background = Color.White,
    onBackground = primary,
    surface = Color.White

    /* Other default colors to override
    onSurface = Color(0xFF1C1B1F),
    */
)

private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = Color.White,
    secondary = secondary,
    onSecondary = primary,
    tertiary = tertiary,
    onTertiary = Color.Black,
    background = Color.White,
    onBackground = primary,
    surface = Color.White

    /* Other default colors to override
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun HarmonyTheme(
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
