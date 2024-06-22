package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

class RemoteRefrigerator : RemoteDevice<RemoteRefrigeratorState>() {

    override fun asModel(): Refrigerator {
        return Refrigerator(
            id = id,
            name = name,
            room = room?.asModel(),
            status = RemoteStatus.asModel(state.status),
            temperature = state.temperature,
            freezerTemperature = state.freezerTemperature,
            mode = state.mode
        )
    }
}