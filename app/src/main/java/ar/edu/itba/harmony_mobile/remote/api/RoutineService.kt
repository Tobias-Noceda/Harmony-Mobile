package ar.edu.itba.harmony_mobile.remote.api

import ar.edu.itba.harmony_mobile.remote.model.RemoteResult
import ar.edu.itba.harmony_mobile.remote.model.RemoteRoutine
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoutineService {

    @GET("routines")
    suspend fun getRoutines(): Response<RemoteResult<List<RemoteRoutine>>>

    @GET("routine/{routineId}")
    suspend fun getRoutine(@Path("routineId") routineId: String): Response<RemoteResult<RemoteRoutine>>

    @POST("routines/{routineId}/execute")
    suspend fun executeRoutine(@Path("routineId") routineId: String): Response<RemoteResult<Boolean>>
}