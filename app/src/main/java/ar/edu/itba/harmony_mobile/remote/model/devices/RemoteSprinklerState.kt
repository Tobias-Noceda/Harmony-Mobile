package ar.edu.itba.harmony_mobile.remote.model.devices

import com.google.gson.annotations.SerializedName

class RemoteSprinklerState {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("quantity")
    var quantity: Int = 0

    @SerializedName("dispensedQuantity")
    var dispensedQuantity: Int = 0

    @SerializedName("unit")
    lateinit var unit: String
}