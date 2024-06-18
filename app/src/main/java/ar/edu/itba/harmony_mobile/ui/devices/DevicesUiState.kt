package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Device

data class DevicesUiState(
    val isFetching: Boolean = false,
    val error: Error? = null,
    val devices: List<Device> = emptyList()
)
