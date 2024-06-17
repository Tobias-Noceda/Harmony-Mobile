package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteLampState
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus
import ar.edu.itba.harmony_mobile.model.Lamp

class RemoteLamp : RemoteDevice<RemoteLampState>() {

    override fun asModel(): Lamp {
        return Lamp(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            color = state.color,
            brightness = state.brightness
        )
    }
}