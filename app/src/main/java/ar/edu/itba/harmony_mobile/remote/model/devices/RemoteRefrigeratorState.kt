package ar.edu.itba.harmony_mobile.remote.model.devices

import com.google.gson.annotations.SerializedName

class RemoteRefrigeratorState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("freezerTemperature")
    var freezerTemperature: Int = 0

    @SerializedName("temperature")
    var temperature: Int = 0

    @SerializedName("mode")
    lateinit var mode: String
}