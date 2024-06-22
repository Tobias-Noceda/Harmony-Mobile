package ar.edu.itba.harmony_mobile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class DeviceTypes(
    @StringRes val type: Int,
    val apiName: String,
    @DrawableRes val icon: Int
) {
    LIGHTS(R.string.lamps, "lamp", R.drawable.lamp),
    DOORS(R.string.doors, "door", R.drawable.door),
    REFRIGERATORS(R.string.refrigerators, "refrigerator", R.drawable.fridge),
    VACUUMS(R.string.vacuums, "vacuum", R.drawable.vacuum),
    SPRINKLERS(R.string.sprinklers, "faucet", R.drawable.sprinkler),
    BLINDS(R.string.blinds, "blinds", R.drawable.blinds)
}