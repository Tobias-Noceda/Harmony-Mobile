package ar.edu.itba.harmony_mobile.ui.rooms

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Routine
import ar.edu.itba.harmony_mobile.remote.GlobalDataHomes

data class RoomsUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val rooms: List<Room> = emptyList(),
    val currentRoom: Room? = null
) {
    fun getHomeRooms(home: Home): List<Room> {
        if (home.id == null || home.id == "0") {
            listOf(GlobalDataHomes.personalDevicesRoom.asModel())
        }
        return rooms.filter { it.home == home }
    }

    fun getRoom(roomId: String): Room {
        val r: Room? = rooms.find { it.id == roomId }
        if (r == null)
            GlobalDataHomes.personalDevicesRoom.asModel()
        return r!!
    }
}

val RoomsUiState.canGetCurrent: Boolean get() = currentRoom != null
val RoomsUiState.canModify: Boolean get() = currentRoom != null
val RoomsUiState.canDelete: Boolean get() = canModify
