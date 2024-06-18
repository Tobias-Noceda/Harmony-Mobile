package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Error

data class SprinklerUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val currentDevice: Sprinkler? = null
)

val SprinklerUiState.canExecuteAction: Boolean get() = currentDevice != null && !loading