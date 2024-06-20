package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Error

data class DoorUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val currentDevice: Door? = null
)

val DoorUiState.canExecuteAction: Boolean get() = currentDevice != null && !loading