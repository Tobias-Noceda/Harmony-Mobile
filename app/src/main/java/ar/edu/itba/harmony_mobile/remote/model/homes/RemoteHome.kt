package ar.edu.itba.harmony_mobile.remote.model.homes

import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import com.google.gson.annotations.SerializedName

class RemoteHome {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("rooms")
    var rooms: MutableSet<RemoteRoom> = HashSet()

    @SerializedName("meta")
    lateinit var meta: RemoteHomeMeta


    fun asModel(): Home {
        return Home(
            id = id,
            name = name,
            rooms = rooms.map { it.asModel() },
            size = meta.size,
            color = meta.color
        )
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteHome

        return id == other.id
    }
}