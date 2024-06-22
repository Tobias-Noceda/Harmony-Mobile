package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

enum class Status {
    ON, OFF, OPEN, OPENED, CLOSED, DOCKED, ACTIVE, INACTIVE;

    companion object {
        fun asRemoteModel(value: Status): String {
            return when (value) {
                ON -> RemoteStatus.ON
                OFF -> RemoteStatus.OFF
                OPEN -> RemoteStatus.OPEN
                OPENED -> RemoteStatus.OPENED
                CLOSED -> RemoteStatus.CLOSED
                ACTIVE -> RemoteStatus.ACTIVE
                INACTIVE -> RemoteStatus.INACTIVE
                DOCKED -> RemoteStatus.DOCKED
            }
        }
    }
}