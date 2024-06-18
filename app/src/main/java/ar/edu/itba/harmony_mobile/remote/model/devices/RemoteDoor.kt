package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus
import ar.edu.itba.harmony_mobile.model.Door

class RemoteDoor : RemoteDevice<RemoteDoorState>() {

    override fun asModel(): Door {
        return Door(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            lock = state.lock
        )
    }
}