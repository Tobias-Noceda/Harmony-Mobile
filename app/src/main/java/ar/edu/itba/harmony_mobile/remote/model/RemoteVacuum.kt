package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteVacuumState
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus
import ar.edu.itba.harmony_mobile.model.Vacuum

class RemoteVacuum : RemoteDevice<RemoteVacuumState>() {

    override fun asModel(): Vacuum {
        return Vacuum(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            targetRoom = state.targetRoom?.asModel(),
            battery = state.battery
        )
    }
}