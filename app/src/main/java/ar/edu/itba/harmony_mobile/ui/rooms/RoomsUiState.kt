package ar.edu.itba.harmony_mobile.ui.rooms

import ar.edu.itba.harmony_mobile.model.Routine
import ar.edu.itba.harmony_mobile.model.Error

data class RoutinesUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val routines: List<Routine> = emptyList(),
    val currentRoutine: Routine? = null
)

val RoutinesUiState.canGetCurrent: Boolean get() = currentRoutine != null
val RoutinesUiState.canModify: Boolean get() = currentRoutine != null
val RoutinesUiState.canDelete: Boolean get() = canModify
