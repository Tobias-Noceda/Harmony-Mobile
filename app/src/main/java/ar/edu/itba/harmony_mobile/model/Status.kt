package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteStatus

enum class Status {
    ON, OFF, OPEN, CLOSED, DOCKED;

    companion object {
        fun asRemoteModel(value: Status): String {
            return when (value) {
                ON -> RemoteStatus.ON
                OFF -> RemoteStatus.OFF
                OPEN -> RemoteStatus.OPEN
                CLOSED -> RemoteStatus.CLOSED
                DOCKED -> RemoteStatus.DOCKED
            }
        }
    }
}