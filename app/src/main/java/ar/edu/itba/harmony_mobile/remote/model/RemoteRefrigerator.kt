package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteRefrigeratorState
import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus
import ar.edu.itba.harmony_mobile.model.Refrigerator

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