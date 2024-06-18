package ar.edu.itba.harmony_mobile.remote.model.rooms

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import com.google.gson.annotations.SerializedName

class RemoteRoom {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("home")
    var home: RemoteHome? = null

    @SerializedName("devices")
    var devices: List<RemoteDevice<*>>? = null

    @SerializedName("meta")
    lateinit var meta: RemoteRoomMeta

    fun asModel(): Room {
        return Room(
            id = id,
            name = name,
            home = home?.asModel(),
            devices = devices?.map { it.asModel() },
            size = meta.size,
            color = meta.color
        )
    }
}