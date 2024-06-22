package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Lamp

data class RefrigeratorUiState(
    val loading: Boolean = false,
    val error: Error? = null
) {
    fun canExecuteAction(refrigerator: Refrigerator, action: Refrigerator.Companion): Boolean {
        return !loading // TODO implement logic for this
    }
}