package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHomeMeta

class Home(
    var id: String? = null,
    var name: String,
    var size: String? = null,
    var color: String? = null
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

    override fun toString(): String {
        return "{Home;id:${id};name:${name}}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Home

        return id == other.id
    }
}