package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteRoutine
import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteRoutineMeta

class Routine(
    var id: String? = null,
    var name: String,
    var actions: List<Any>,
    var icon: String,
    var homeId: String
) {

    fun asRemoteModel(): RemoteRoutine {
        val meta = RemoteRoutineMeta()
        meta.icon = icon
        meta.homeId = homeId

        val model = RemoteRoutine()
        model.id = id
        model.name = name
        model.actions = actions
        model.meta = meta

        return model
    }
}