package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoomMeta

class Room(
    var id: String? = null,
    var name: String,
    var home: Home?,
    var size: String? = null,
    var color: String? = null
) {

    fun asRemoteModel(): RemoteRoom {
        val meta = RemoteRoomMeta()
        meta.size = size
        meta.color = color

        val model = RemoteRoom()
        model.id = id
        model.name = name
        model.home = home?.asRemoteModel()
        model.meta = meta

        return model
    }

    override fun toString(): String {
        return "{Room;id:${id};name:${name};Home:${home?.name}}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Room

        return id == other.id
    }
}