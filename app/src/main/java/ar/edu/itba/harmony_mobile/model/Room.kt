package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteRoom
import ar.edu.itba.harmony_mobile.remote.model.RemoteRoomMeta

class Room(
    var id: String? = null,
    var name: String,
    var home: Home?,
    var size: String,
    var color: String
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
}