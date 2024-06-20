package ar.edu.itba.harmony_mobile.ui.rooms

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Routine

data class RoomsUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val rooms: List<Room> = emptyList(),
    val currentRoom: Room? = null
) {
    fun getHomeRooms(home: Home): List<Room> {
        if (home.id == null || home.id == "0") {
            return rooms.filter { it.home == null || it.home == home }
        }
        return rooms.filter { it.home == home }
    }
}

val RoomsUiState.canGetCurrent: Boolean get() = currentRoom != null
val RoomsUiState.canModify: Boolean get() = currentRoom != null
val RoomsUiState.canDelete: Boolean get() = canModify
