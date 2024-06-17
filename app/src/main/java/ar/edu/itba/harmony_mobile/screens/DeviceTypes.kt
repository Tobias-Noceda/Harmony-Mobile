package ar.edu.itba.harmony_mobile.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector
import ar.edu.itba.harmony_mobile.R

enum class DeviceTypes(
    @StringRes val type: Int,
    @StringRes val apiName: Int,
    val icon: ImageVector
) {
    LIGHTS(R.string.lamp, R.string.api_lamp, Icons.Default.Lock),
    DOORS(R.string.door, R.string.api_door, Icons.Default.Lock),
    REFRIS(R.string.refrigerator, R.string.api_refrigerator, Icons.Default.Lock),
    VACUUMS(R.string.vacuum, R.string.api_vacuum, Icons.Default.Lock),
    SPRINKLERS(R.string.sprinkler, R.string.api_sprinkler, Icons.Default.Lock),
    BLINDS(R.string.blinds, R.string.api_blinds, Icons.Default.Lock)
}