package ar.edu.itba.harmony_mobile.remote.model.rooms

import com.google.gson.annotations.SerializedName

class RemoteRoomMeta {
    @SerializedName("size")
    var size: String? = null

    @SerializedName("color")
    var color: String? = null
}