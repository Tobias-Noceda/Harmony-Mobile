package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteRoutine
import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteActionMeta

class Routine(
    var id: String? = null,
    var name: String,
    var actions: List<Action>,
    var icon: String,
    var homeId: String
) {

    fun asRemoteModel(): RemoteRoutine {
        val meta = RemoteActionMeta()
        meta.icon = icon
        meta.homeId = homeId

        val model = RemoteRoutine()
        model.id = id
        model.name = name
        model.actions = actions.map { it.asRemoteModel() }
        model.actions.forEach { it.meta.icon = icon;it.meta.homeId = homeId }

        return model
    }

    override fun toString(): String {
        return "{Routine;id:${id};name:${name};icon:${icon};homeId:${homeId};actions:${actions}}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Routine

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}