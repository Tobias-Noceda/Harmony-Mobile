package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.model.Lamp.Companion.colorToString
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteSprinkler
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteSprinklerState

class Sprinkler(
    id: String?,
    name: String,
    room: Room?,
    val status: Status,
    val quantity: Int = 0,
    val dispensedQuantity: Int = 0,
    val unit: String
) : Device(id, name, room, DeviceTypes.SPRINKLERS) {

    override fun asRemoteModel(): RemoteDevice<RemoteSprinklerState> {
        val state = RemoteSprinklerState()
        state.status = Status.asRemoteModel(status)
        state.quantity = quantity
        state.dispensedQuantity = dispensedQuantity
        state.unit = unit

        val model = RemoteSprinkler()
        model.id = id
        model.name = name
        model.room = room?.asRemoteModel()
        model.setState(state)
        return model
    }

    override fun toString(): String {
        return "{Sprinkler;id:${id};name:${name};Room:${room?.name};Status:${status};quantity:${quantity},dQuantity:${dispensedQuantity};unit:${unit}}"
    }

    companion object {
        const val OPEN_ACTION = "open"
        const val CLOSE_ACTION = "close"
        const val DISPENSE_ACTION = "dispense"
    }
}