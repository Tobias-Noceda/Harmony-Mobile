package ar.edu.itba.harmony_mobile.remote.model.devices

import com.google.gson.annotations.SerializedName

class RemoteBlindsState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("currentLevel")
    var currentLevel: Int = 0

    @SerializedName("level")
    var level: Int = 0
}