package ar.edu.itba.harmony_mobile.remote.model.devices

import com.google.gson.annotations.SerializedName

class RemoteDoorState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("lock")
    var lock: Boolean = false
}