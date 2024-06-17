package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.RemoteSprinklerState
import ar.edu.itba.harmony_mobile.remote.model.RemoteSprinkler

class Sprinkler(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
) : Device(id, name, room,DeviceType.SPRINKLER) {

    override fun asRemoteModel(): RemoteDevice<RemoteSprinklerState> {
        val state = RemoteSprinklerState()
        state.status = Status.asRemoteModel(status)

        val model = RemoteSprinkler()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    companion object {
        const val TURN_ON_ACTION = "turnOn"
        const val TURN_OFF_ACTION = "turnOff"
        const val DISPENSE_ACTION = "dispense"
    }
}