package ar.edu.itba.harmony_mobile.remote.model.homes

import com.google.gson.annotations.SerializedName

class RemoteHomeMeta {
    @SerializedName("size")
    var size: String? = null

    @SerializedName("color")
    var color: String? = null
}