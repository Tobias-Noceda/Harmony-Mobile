package ar.edu.itba.harmony_mobile.remote.model.routines;

import ar.edu.itba.harmony_mobile.model.Action
import com.google.gson.annotations.SerializedName

class RemoteAction {
    @SerializedName("meta")
    var meta: RemoteActionMeta = RemoteActionMeta()

    fun asModel(): Action {
        return Action()
    }
}
