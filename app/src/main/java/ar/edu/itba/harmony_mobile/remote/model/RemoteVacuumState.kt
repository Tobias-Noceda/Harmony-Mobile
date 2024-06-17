package ar.edu.itba.harmony_mobile.remote.model

import com.google.gson.annotations.SerializedName

class RemoteVacuumState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("targetRoom")
    var targetRoom: RemoteRoom? = null

    @SerializedName("battery")
    var battery: Int = 0
}