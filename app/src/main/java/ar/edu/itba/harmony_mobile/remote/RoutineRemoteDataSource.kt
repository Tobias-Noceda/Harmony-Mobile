package ar.edu.itba.harmony_mobile.remote

import ar.edu.itba.harmony_mobile.remote.api.RoutineService
import ar.edu.itba.harmony_mobile.remote.model.routines.RemoteRoutine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoutineRemoteDataSource(
    private val routineService: RoutineService
) : RemoteDataSource() {

    val routines: Flow<List<RemoteRoutine>> = flow {
        while (true) {
            val devices = handleApiResponse {
                routineService.getRoutines()
            }
            emit(devices)
            delay(DELAY)
        }
    }

    suspend fun getRoutine(routineId: String): RemoteRoutine {
        return handleApiResponse {
            routineService.getRoutine(routineId)
        }
    }

    suspend fun executeRoutine(routineId: String): Boolean {
        return handleApiResponse {
            routineService.executeRoutine(routineId)
        }
    }

    companion object {
        const val DELAY: Long = 10000
    }
}