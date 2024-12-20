package ar.edu.itba.harmony_mobile.remote.model

import com.google.gson.annotations.SerializedName

data class EventData(
    @SerializedName("timestamp") var timestamp: String,
    @SerializedName("deviceId") var deviceId: String,
    @SerializedName("event") var event: String,
    @SerializedName("args") var args: Map<String, String> = mapOf()
)
