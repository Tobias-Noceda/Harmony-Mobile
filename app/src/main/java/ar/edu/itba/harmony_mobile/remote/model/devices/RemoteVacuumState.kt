package ar.edu.itba.harmony_mobile.remote.model.devices

import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import com.google.gson.annotations.SerializedName

class RemoteVacuumState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("mode")
    lateinit var mode: String

    @SerializedName("location")
    var targetRoom: RemoteRoom? = null

    @SerializedName("battery")
    var battery: Int = 0
}