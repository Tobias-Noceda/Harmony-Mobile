package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.RemoteRefrigerator
import ar.edu.itba.harmony_mobile.remote.model.RemoteRefrigeratorState

class Refrigerator(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val temperature: Int,
    val freezerTemperature: Int,
    val mode: String
) : Device(id, name, room, DeviceType.REFRIGERATOR) {

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

    companion object {
        const val TURN_ON_ACTION = "turnOn"
        const val TURN_OFF_ACTION = "turnOff"
    }
}