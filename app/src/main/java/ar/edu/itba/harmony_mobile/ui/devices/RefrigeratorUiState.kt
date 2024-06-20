package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Error

data class RefrigeratorUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val currentDevice: Refrigerator? = null
)

val RefrigeratorUiState.canExecuteAction: Boolean get() = currentDevice != null && !loading