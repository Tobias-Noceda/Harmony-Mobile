package ar.edu.itba.harmony_mobile.remote.model.routines

import ar.edu.itba.harmony_mobile.model.Routine
import com.google.gson.annotations.SerializedName

class RemoteRoutine {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("actions")
    var actions: List<Any> = ArrayList()//TODO create an action type, probably

    @SerializedName("meta")
    var meta: RemoteRoutineMeta = RemoteRoutineMeta()


    fun asModel(): Routine {
        return Routine(
            id = id,
            name = name,
            actions = actions,
            icon = meta.icon,
            homeId = meta.homeId
        )
    }
}