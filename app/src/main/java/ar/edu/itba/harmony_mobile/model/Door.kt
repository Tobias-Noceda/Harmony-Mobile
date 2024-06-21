package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.model.Lamp.Companion.colorToString
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDoor
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDoorState

class Door(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val lock: Boolean
) : Device(id, name, room, DeviceTypes.DOORS) {

    override fun asRemoteModel(): RemoteDevice<RemoteDoorState> {
        val state = RemoteDoorState()
        state.status = Status.asRemoteModel(status)
        state.lock = lock

        val model = RemoteDoor()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    override fun toString(): String {
        return "{Door;id:${id};name:${name};Room:${room?.name};Status:${status};lock:${lock}}"
    }

    companion object {
        const val OPEN_ACTION = "open"
        const val CLOSE_ACTION = "close"
        const val LOCK_ACTION = "lock"
        const val UNLOCK_ACTION = "unlock"
    }
}