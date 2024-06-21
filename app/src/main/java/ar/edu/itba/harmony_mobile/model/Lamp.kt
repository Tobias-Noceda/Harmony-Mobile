package ar.edu.itba.harmony_mobile.model

import androidx.compose.ui.graphics.Color
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteLamp
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteLampState

class Lamp(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val color: Color,
    val brightness: Int
) : Device(id, name, room, DeviceTypes.LIGHTS) {

    override fun asRemoteModel(): RemoteDevice<RemoteLampState> {
        val state = RemoteLampState()
        state.status = Status.asRemoteModel(status)
        state.color = color.toString()
        state.brightness = brightness

        val model = RemoteLamp()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    companion object {
        const val TURN_ON_ACTION = "turnOn"
        const val TURN_OFF_ACTION = "turnOff"
        const val SET_COLOR_ACTION = "setColor"
        const val SET_BRIGHTNESS_ACTION = "setBrightness"
    }
}