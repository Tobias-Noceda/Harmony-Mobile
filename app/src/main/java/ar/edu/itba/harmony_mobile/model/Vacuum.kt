package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.model.Lamp.Companion.colorToString
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteVacuum
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteVacuumState

class Vacuum(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val mode: String,
    val targetRoom: Room?,
    val battery: Int,
) : Device(id, name, room, DeviceTypes.VACUUMS) {

    override fun asRemoteModel(): RemoteDevice<RemoteVacuumState> {
        val state = RemoteVacuumState()
        state.status = Status.asRemoteModel(status)
        state.targetRoom = targetRoom?.asRemoteModel()
        state.battery = battery
        state.mode = mode

        val model = RemoteVacuum()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    override fun toString(): String {
        return "{Vacuum;id:${id};name:${name};Room:${room?.name};Status:${status};mode:${mode},targetRoom:${targetRoom};battery:${battery}}"
    }

    companion object {
        const val START_ACTION = "start"
        const val PAUSE_ACTION = "pause"
        const val DOCK_ACTION = "dock"
        const val SET_LOCATION_ACTION = "setLocation"
        const val SET_MODE_ACTION = "setMode"
    }
}