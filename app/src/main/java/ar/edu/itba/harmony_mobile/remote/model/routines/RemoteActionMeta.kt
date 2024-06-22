package ar.edu.itba.harmony_mobile.remote.model.routines

import com.google.gson.annotations.SerializedName

class RemoteActionMeta {
    @SerializedName("homeId")
    var homeId: String = ""

    @SerializedName("icon")
    var icon: String = ""
}