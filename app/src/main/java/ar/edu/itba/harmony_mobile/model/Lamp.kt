package ar.edu.itba.harmony_mobile.model

import androidx.compose.ui.graphics.Color
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteLamp
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteLampState
import okhttp3.internal.toHexString

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
        state.color = colorToString(color)
        state.brightness = brightness

        val model = RemoteLamp()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    override fun toString(): String {
        return "{Lamp;id:${id};name:${name};Room:${room?.name};Status:${status};color:${colorToString(color)},brightness:${brightness}}"
    }

    companion object {
        const val TURN_ON_ACTION = "turnOn"
        const val TURN_OFF_ACTION = "turnOff"
        const val SET_COLOR_ACTION = "setColor"
        const val SET_BRIGHTNESS_ACTION = "setBrightness"
        fun colorToString(color: Color): String {
            var red = (0xFF * color.red.toInt()).toHexString()
            if(red.length<2)
                red = "0${red}"
            var blue = (0xFF * color.blue.toInt()).toHexString()
            if(blue.length<2)
                blue = "0${blue}"
            var green = (0xFF * color.green.toInt()).toHexString()
            if(green.length<2)
                green = "0${green}"
            return "${red}${blue}${green}"
        }
    }
}