package ar.edu.itba.harmony_mobile.remote.model

import ar.edu.itba.harmony_mobile.remote.model.RemoteHomeMeta
import ar.edu.itba.harmony_mobile.model.Home
import com.google.gson.annotations.SerializedName

class RemoteHome {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("home")
    var home: RemoteHome? = null

    @SerializedName("meta")
    lateinit var meta: RemoteHomeMeta


    fun asModel(): Home {
        return Home(
            id = id,
            name = name,
            size = meta.size,
            color = meta.color
        )
    }
}