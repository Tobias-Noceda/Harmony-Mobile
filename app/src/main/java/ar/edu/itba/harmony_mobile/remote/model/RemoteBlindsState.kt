package ar.edu.itba.harmony_mobile.remote.model

import com.google.gson.annotations.SerializedName

class RemoteBlindsState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("currentLevel")
    var currentLevel: Int = 0
}