package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Lamp

data class DoorUiState(
    val loading: Boolean = false,
    val error: Error? = null,
) {
    fun canExecuteAction(door: Door, action: Door.Companion): Boolean {
        return !loading // TODO implement logic for this
    }
}