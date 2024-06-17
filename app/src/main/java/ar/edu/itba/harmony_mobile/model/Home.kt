package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteHome
import ar.edu.itba.harmony_mobile.remote.model.RemoteHomeMeta

class Home(
        var id: String? = null,
        var name: String,
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
        model.meta = meta

        return model
    }
}