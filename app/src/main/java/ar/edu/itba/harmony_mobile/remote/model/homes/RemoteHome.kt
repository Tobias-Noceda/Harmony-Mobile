package ar.edu.itba.harmony_mobile.remote.model.homes

import android.util.Log
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import com.google.gson.annotations.SerializedName

class RemoteHome {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String = ""

    @SerializedName("rooms")
    var rooms: MutableSet<RemoteRoom> = HashSet()

    @SerializedName("meta")
    var meta: RemoteHomeMeta? = null


    fun asModel(): Home {
        return Home(
            id = id,
            name = name,
            rooms = rooms.map { it.asModel() },
            size = meta?.size,
            color = meta?.color
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

    override fun toString(): String {
        return "{RemoteHome;id:${id};name:${name};Rooms:${rooms.fold("") { acc, room -> "${acc}${room.toString()};" }}}"
    }
}