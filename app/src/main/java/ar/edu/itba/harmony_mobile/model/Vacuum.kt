package ar.edu.itba.harmony_mobile.model

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
) : Device(id, name, room, DeviceType.VACUUM) {

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

    companion object {
        const val TURN_ON_ACTION = "turnOn"
        const val TURN_OFF_ACTION = "turnOff"
    }
}