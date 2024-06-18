package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class RemoteDevice<T> where T : Any {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("type")
    @Expose(serialize = false)
    lateinit var type: RemoteDeviceType

    @SerializedName("room")
    var room: RemoteRoom? = null

    @Expose(serialize = false)
    lateinit var state: T
        private set

    @SerializedName("meta")
    var meta: Any? = null

    fun setState(state: T) {
        this.state = state
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    abstract fun asModel(): Device

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteDevice<*>

        return id == other.id
    }
}