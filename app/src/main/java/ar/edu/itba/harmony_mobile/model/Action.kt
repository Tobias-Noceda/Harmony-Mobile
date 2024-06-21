package ar.edu.itba.harmony_mobile.model;

import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteAction

class Action {

    fun asRemoteModel(): RemoteAction {
        return RemoteAction()
    }
}
