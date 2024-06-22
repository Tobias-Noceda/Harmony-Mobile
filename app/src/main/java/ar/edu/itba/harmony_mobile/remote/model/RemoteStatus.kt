package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.model.Status

object RemoteStatus {
    const val ON = "on"
    const val OFF = "off"
    const val OPEN = "open"
    const val OPENED = "opened"
    const val CLOSED = "closed"
    const val DOCKED = "docked"
    const val ACTIVE = "active"
    const val INACTIVE = "inactive"

    fun asModel(status: String): Status {
        return when (status) {
            ON -> Status.ON
            OFF -> Status.OFF
            OPEN -> Status.OPEN
            OPENED -> Status.OPENED
            CLOSED -> Status.CLOSED
            ACTIVE -> Status.ACTIVE
            INACTIVE -> Status.INACTIVE
            else -> Status.DOCKED
        }
    }
}