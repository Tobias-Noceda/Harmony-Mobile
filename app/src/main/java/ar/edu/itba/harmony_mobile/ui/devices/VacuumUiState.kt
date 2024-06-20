package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.model.Error

data class VacuumUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val currentDevice: Vacuum? = null
)

val VacuumUiState.canExecuteAction: Boolean get() = currentDevice != null && !loading