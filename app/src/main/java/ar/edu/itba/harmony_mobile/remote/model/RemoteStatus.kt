package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.model.Status

object RemoteStatus {
    const val ON = "on"
    const val OFF = "off"

    fun asModel(status: String): Status {
        return when (status) {
            ON -> Status.ON
            else -> Status.OFF
        }
    }
}