package ar.edu.itba.harmony_mobile.ui.devices

import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Room

data class DevicesUiState(
    val isFetching: Boolean = false,
    val error: Error? = null,
    val devices: List<Device> = emptyList()
) {
    fun getHomeDevices(home: Home): List<Device> {
        if (home.id == null || home.id == "0") {
            return devices.filter { it.room?.home == null || it.room.home == home }
        }
        return devices.filter { it.room?.home != null && it.room.home!! == home }
    }

    fun getRoomDevices(room: Room): List<Device> {
        if (room.id == null || room.id == "0") {
            return devices.filter { it.room == null }
        }
        return devices.filter { it.room != null && it.room == room }
    }
}
