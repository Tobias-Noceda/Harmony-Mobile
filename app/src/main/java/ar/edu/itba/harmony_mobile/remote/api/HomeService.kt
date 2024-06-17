package ar.edu.itba.harmony_mobile.remote.api

import ar.edu.itba.harmony_mobile.remote.model.RemoteResult
import ar.edu.itba.harmony_mobile.remote.model.RemoteHome
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HomeService {

    @GET("homes")
    suspend fun getHomes(): Response<RemoteResult<List<RemoteHome>>>

    @POST("homes")
    suspend fun addHome(@Body home: RemoteHome): Response<RemoteResult<RemoteHome>>

    @GET("homes/{homeId}")
    suspend fun getHome(@Path("homeId") homeId: String): Response<RemoteResult<RemoteHome>>

    @PUT("homes/{homeId}")
    suspend fun modifyHome(
        @Path("homeId") homeId: String,
        @Body home: RemoteHome
    ): Response<RemoteResult<Boolean>>

    @DELETE("homes/{homeId}")
    suspend fun deleteHome(@Path("homeId") homeId: String): Response<RemoteResult<Boolean>>
}