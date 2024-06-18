package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Error

data class LampUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val currentDevice: Lamp? = null
)

val LampUiState.canExecuteAction: Boolean get() = currentDevice != null && !loading