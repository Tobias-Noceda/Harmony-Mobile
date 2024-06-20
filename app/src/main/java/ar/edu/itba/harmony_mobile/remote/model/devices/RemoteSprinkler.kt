package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

class RemoteSprinkler : RemoteDevice<RemoteSprinklerState>() {

    override fun asModel(): Sprinkler {
        return Sprinkler(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            quantity = state.quantity,
            dispensedQuantity = state.dispensedQuantity,
            unit = state.unit
        )
    }
}