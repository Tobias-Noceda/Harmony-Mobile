package ar.edu.itba.harmony_mobile.remote.model

import com.google.gson.annotations.SerializedName

class RemoteSprinklerState {
    @SerializedName("status")
    lateinit var status: String
}