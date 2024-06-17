package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteRoomMeta
import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.remote.model.RemoteHome
import com.google.gson.annotations.SerializedName

class RemoteRoom {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("home")
    var home: RemoteHome? = null

    @SerializedName("meta")
    lateinit var meta: RemoteRoomMeta

    fun asModel() : Room {
        return Room(
            id = id,
            name = name,
            home = home?.asModel(),
            size = meta.size,
            color = meta.color
        )
    }
}