package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Lamp

data class SprinklerUiState(
    val loading: Boolean = false,
    val error: Error? = null,
) {
    fun canExecuteAction(sprinkler: Sprinkler, action: Sprinkler.Companion): Boolean {
        return !loading // TODO implement logic for this
    }
}