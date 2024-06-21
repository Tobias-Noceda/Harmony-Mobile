package ar.edu.itba.harmony_mobile.ui.routines

import ar.edu.itba.harmony_mobile.model.Routine
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Home

data class RoutinesUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val routines: List<Routine> = emptyList(),
    val currentRoutine: Routine? = null
) {
    fun getRoutine(routineId: String): Routine? {
        return routines.find { it.id == routineId }
    }

    fun getHomeRoutines(home: Home): List<Routine> {
        if (home.id == null || home.id == "0") {
            return routines.filter { it.homeId == "0" }
        }
        return routines.filter { it.homeId == home.id }
    }
}

val RoutinesUiState.canGetCurrent: Boolean get() = currentRoutine != null
val RoutinesUiState.canModify: Boolean get() = currentRoutine != null
val RoutinesUiState.canDelete: Boolean get() = canModify
