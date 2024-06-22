package ar.edu.itba.harmony_mobile.remote.model.devices

import com.google.gson.annotations.SerializedName

class RemoteDoorState {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("lock")
    var lock: String = ""
}