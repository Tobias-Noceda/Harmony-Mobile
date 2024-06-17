package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteSprinkler
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus
import ar.edu.itba.harmony_mobile.model.Sprinkler

class RemoteSprinkler : RemoteDevice<RemoteSprinklerState>() {

    override fun asModel(): Sprinkler {
        return Sprinkler(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
        )
    }
}