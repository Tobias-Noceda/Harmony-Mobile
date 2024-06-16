package ar.edu.itba.harmony_mobile.ui.theme

import androidx.compose.ui.graphics.Color

val primary = Color(0xFF273043)
val secondary = Color(0xFFE0E0E2)
val tertiary = Color(0xFF2A9D8F)
val disabled = Color(0xFF888888)

fun Color.darken(factor: Float): Color {
    return this.copy(
        red = red * factor,
        green = green * factor,
        blue = blue * factor
    )
}