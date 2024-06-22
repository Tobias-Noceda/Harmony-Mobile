package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Lamp

data class VacuumUiState(
    val loading: Boolean = false,
    val error: Error? = null,
){
    fun canExecuteAction(vacuum: Vacuum, action: Vacuum.Companion): Boolean {
        return !loading // TODO implement logic for this
    }
}