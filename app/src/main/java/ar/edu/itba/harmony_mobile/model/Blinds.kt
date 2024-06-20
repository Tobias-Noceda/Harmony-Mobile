package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteBlinds
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteBlindsState

class Blinds(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val currentLevel: Int,
    val level: Int,
) : Device(id, name, room,DeviceType.BLINDS) {

    override fun asRemoteModel(): RemoteDevice<RemoteBlindsState> {
        val state = RemoteBlindsState()
        state.status = Status.asRemoteModel(status)
        state.currentLevel = currentLevel
        state.level = level

        val model = RemoteBlinds()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    companion object {
        const val OPEN_ACTION = "open"
        const val CLOSE_ACTION = "close"
    }
}