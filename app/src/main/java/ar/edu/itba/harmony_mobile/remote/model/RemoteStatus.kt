package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.model.Status

object RemoteStatus {
    const val ON = "on"
    const val OFF = "off"
    const val OPEN = "open"
    const val CLOSED = "closed"

    fun asModel(status: String): Status {
        return when (status) {
            ON -> Status.ON
            OFF -> Status.OFF
            OPEN -> Status.OPEN
            else -> Status.CLOSED
        }
    }
}