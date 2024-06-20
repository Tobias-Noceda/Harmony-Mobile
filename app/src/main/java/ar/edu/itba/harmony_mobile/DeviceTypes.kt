package ar.edu.itba.harmony_mobile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class DeviceTypes(
    @StringRes val type: Int,
    @StringRes val apiName: Int,
    @DrawableRes val icon: Int
) {
    LIGHTS(R.string.lamps, R.string.api_lamp, R.drawable.lamp),
    DOORS(R.string.doors, R.string.api_door, R.drawable.door),
    REFRIS(R.string.refrigerators, R.string.api_refrigerator, R.drawable.fridge),
    VACUUMS(R.string.vacuums, R.string.api_vacuum, R.drawable.vacuum),
    SPRINKLERS(R.string.sprinklers, R.string.api_sprinkler, R.drawable.sprinkler),
    BLINDS(R.string.blinds, R.string.api_blinds, R.drawable.blinds)
}