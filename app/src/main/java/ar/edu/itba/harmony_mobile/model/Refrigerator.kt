package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.model.Lamp.Companion.colorToString
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteRefrigerator
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteRefrigeratorState

class Refrigerator(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val temperature: Int,
    val freezerTemperature: Int,
    val mode: String
) : Device(id, name, room, DeviceTypes.REFRIGERATORS) {

    override fun asRemoteModel(): RemoteDevice<RemoteRefrigeratorState> {
        val state = RemoteRefrigeratorState()
        state.status = Status.asRemoteModel(status)
        state.temperature = temperature
        state.freezerTemperature=freezerTemperature
        state.mode=mode

        val model = RemoteRefrigerator()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    override fun toString(): String {
        return "{Refrigerator;id:${id};name:${name};Room:${room?.name};Status:${status};temp:${temperature},fTemp:${freezerTemperature};mode:${mode}}"
    }

    companion object {
        const val TURN_ON_ACTION = "turnOn"
        const val TURN_OFF_ACTION = "turnOff"
        const val SET_TEMPERATURE_ACTION = "setTemperature"
        const val SET_FREEZER_TEMPERATURE_ACTION = "setFreezerTemperature"
        const val SET_MODE_ACTION = "setMode"
    }
}