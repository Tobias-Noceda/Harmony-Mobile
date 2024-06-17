package ar.edu.itba.harmony_mobile.remote.model

import com.google.gson.annotations.SerializedName

class RemoteRoutineMeta {
    @SerializedName("homeId")
    lateinit var homeId: String

    @SerializedName("icon")
    lateinit var icon: String
}