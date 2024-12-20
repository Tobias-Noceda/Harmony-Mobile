package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus
import ar.edu.itba.harmony_mobile.model.Blinds

class RemoteBlinds : RemoteDevice<RemoteBlindsState>() {

    override fun asModel(): Blinds {
        return Blinds(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            currentLevel = state.currentLevel,
            level = state.level
        )
    }
}