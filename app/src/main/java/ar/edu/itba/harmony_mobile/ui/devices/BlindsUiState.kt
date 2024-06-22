package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Lamp

data class BlindsUiState(
    val loading: Boolean = false,
    val error: Error? = null
) {
    fun canExecuteAction(blinds: Blinds, action: Blinds.Companion): Boolean {
        return !loading // TODO implement logic for this
    }
}