package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Error

data class BlindsUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val currentDevice: Blinds? = null
)

val BlindsUiState.canExecuteAction: Boolean get() = currentDevice != null && !loading