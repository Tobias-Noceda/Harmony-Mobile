package ar.edu.itba.harmony_mobile.ui.devices

import android.util.Log
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
        Log.i("LucasH", home.toString())
        Log.i("LucassH", devices.toString())
        if (home.id == null || home.id == "0") {
            return devices.filter { it.room?.home == null || it.room.home == home }
        }
        return devices.filter { it.room?.home != null && it.room.home!! == home }
    }

    fun getRoomDevices(room: Room): List<Device> {
        Log.i("Lucas", room.toString())
        Log.i("Lucass", devices.toString())
        if (room.id == null || room.id == "0") {
            return devices.filter { it.room == null || it.room==room }
        }
        return devices.filter { it.room != null && it.room == room }
    }
}
