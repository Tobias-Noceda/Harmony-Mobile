package ar.edu.itba.harmony_mobile.remote.model.devices

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

class RemoteLamp : RemoteDevice<RemoteLampState>() {

    override fun asModel(): Lamp {
        var aux: Color;
        try {
            aux = Color("#ff${state.color}".toColorInt())
        } catch (e: NumberFormatException) {
            Log.e("color could not be assigned", state.color)
            aux = Color(1.0F, 1.0F, 1.0F, 1.0F)
        }
        return Lamp(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            color = aux,
            brightness = state.brightness
        )
    }
}