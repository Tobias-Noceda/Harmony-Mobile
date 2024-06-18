package ar.edu.itba.harmony_mobile.ui.rooms

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.model.Error

data class RoomsUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val rooms: List<Room> = emptyList(),
    val currentRoom: Room? = null
)

val RoomsUiState.canGetCurrent: Boolean get() = currentRoom != null
val RoomsUiState.canModify: Boolean get() = currentRoom != null
val RoomsUiState.canDelete: Boolean get() = canModify
