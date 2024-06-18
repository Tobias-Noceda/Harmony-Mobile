package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoomMeta

class Room(
    var id: String? = null,
    var name: String,
    var home: Home?,
    var devices: List<Device>?,
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
        model.devices = devices?.map { it.asRemoteModel() }
        model.home = home?.asRemoteModel()
        model.meta = meta

        return model
    }
}