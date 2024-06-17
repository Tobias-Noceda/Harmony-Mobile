package ar.edu.itba.harmony_mobile.repository

import ar.edu.itba.harmony_mobile.model.Routine
import ar.edu.itba.harmony_mobile.remote.RoutineRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoutineRepository(
    private val remoteDataSource: RoutineRemoteDataSource
) {
    val routines: Flow<List<Routine>> = remoteDataSource.routines
        .map { it.map { jt -> jt.asModel() } }


    suspend fun getRoutine(routineId: String): Routine {
        return remoteDataSource.getRoutine(routineId).asModel()
    }

    suspend fun executeRoutine(routineId: String): Boolean {
        return remoteDataSource.executeRoutine(routineId)
    }
}