package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHomeMeta

class Home(
        var id: String? = null,
        var name: String,
        var rooms: List<Room>?,
        var size: String,
        var color: String
) {

    fun asRemoteModel(): RemoteHome {
        val meta = RemoteHomeMeta()
        meta.size = size
        meta.color = color

        val model = RemoteHome()
        model.id = id
        model.name = name
        model.rooms = rooms?.map { it.asRemoteModel() }
        model.meta = meta

        return model
    }
}