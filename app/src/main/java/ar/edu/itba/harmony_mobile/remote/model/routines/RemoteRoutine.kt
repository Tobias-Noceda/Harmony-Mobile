package ar.edu.itba.harmony_mobile.remote.model.routines

import android.util.Log
import ar.edu.itba.harmony_mobile.model.Routine
import com.google.gson.annotations.SerializedName

class RemoteRoutine {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("actions")
    var actions: List<RemoteAction> = ArrayList()//TODO create an action type, probably

    override fun toString(): String {
        return "{Routine;id:${id};name:${name};icon:${actions[0].meta.icon};homeId:${actions[0].meta.homeId};actions:${actions}}"
    }


    fun asModel(): Routine {
        Log.i("RemoteR",this.toString())
        return Routine(
            id = id,
            name = name,
            actions = actions.map { it.asModel() },
            icon = actions[0].meta.icon,
            homeId = actions[0].meta.homeId
        )
    }
}