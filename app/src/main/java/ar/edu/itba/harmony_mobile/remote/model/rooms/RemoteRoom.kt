package ar.edu.itba.harmony_mobile.remote.model.rooms

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import com.google.gson.annotations.SerializedName

class RemoteRoom {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String = ""

    @SerializedName("home")
    var home: RemoteHome? = null

    @SerializedName("devices")
    var devices: MutableSet<RemoteDevice<*>> = HashSet()

    @SerializedName("meta")
    var meta: RemoteRoomMeta? = null

    fun asModel(): Room {
        return Room(
            id = id,
            name = name,
            home = home?.asModel(),
            devices = devices.map { it.asModel() },
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

        other as RemoteRoom

        return id == other.id
    }

    override fun toString(): String {
        return "{RemoteRoom;id:${id};name:${name};Home:${home?.name};devices:${devices.fold("") { acc, device -> "${acc}${device.toString()};" }}}"
    }
}