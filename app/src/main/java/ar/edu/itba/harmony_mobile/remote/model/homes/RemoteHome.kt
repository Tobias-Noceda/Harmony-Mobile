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
    var rooms: List<RemoteRoom>? = null

    @SerializedName("meta")
    lateinit var meta: RemoteHomeMeta


    fun asModel(): Home {
        return Home(
            id = id,
            name = name,
            rooms = rooms?.map { it.asModel() },
            size = meta.size,
            color = meta.color
        )
    }
}