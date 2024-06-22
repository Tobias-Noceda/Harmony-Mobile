package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

class RemoteVacuum : RemoteDevice<RemoteVacuumState>() {

    override fun asModel(): Vacuum {
        return Vacuum(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            mode = state.mode,
            targetRoom = state.targetRoom?.asModel(),
            battery = state.battery
        )
    }
}