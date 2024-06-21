package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Error

data class LampUiState(
    val loading: Boolean = false,
    val error: Error? = null,
) {
    fun canExecuteAction(lamp: Lamp, action: Lamp.Companion): Boolean {
        return !loading // TODO implement logic for this
    }
}