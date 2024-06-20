package ar.edu.itba.harmony_mobile.remote.model.devices

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

class RemoteLamp : RemoteDevice<RemoteLampState>() {

    override fun asModel(): Lamp {
        return Lamp(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            color = Color(state.color.toColorInt()),
            brightness = state.brightness
        )
    }
}