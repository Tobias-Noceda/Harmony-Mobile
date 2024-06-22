package ar.edu.itba.harmony_mobile.model;

import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteAction

class Action(
    var deviceName: String,
    var deviceId: String,
    var actionName: String,
    var params: List<String> = ArrayList()
) {

    fun asRemoteModel(): RemoteAction {
        val action = RemoteAction()
        val aux = RemoteAction.AuxDevice()
        aux.name = deviceName
        aux.id = deviceId
        action.device = aux
        action.params = params
        action.actionName = actionName
        return action;
    }

    override fun toString(): String {
        return "{Action;name:${actionName};params:${params};deviceName:${deviceName};deviceId:${deviceId}}"
    }
}
