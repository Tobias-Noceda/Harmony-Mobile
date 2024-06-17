package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.RemoteDoor
import ar.edu.itba.harmony_mobile.remote.model.RemoteDoorState

class Door(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val lock: Boolean
) : Device(id, name, room, DeviceType.DOOR) {

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

    companion object {
        const val OPEN_ACTION = "open"
        const val CLOSE_ACTION = "close"
        const val LOCK_ACTION = "lock"
        const val UNLOCK_ACTION = "unlock"
    }
}