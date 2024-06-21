package ar.edu.itba.harmony_mobile.remote.api

import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

public const val API_URL = "http://10.0.2.2:8080/api/" // if you are using an emulator
// public const val API_URL = "http://192.168.0.24:8080/api/" // if you are not using an emulator
// public  const val API_URL = "https://api-hci-9716.onrender.com/api/" // Hosted api

private val httpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BASIC)

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .build()

private val gson = GsonBuilder()
    .registerTypeAdapter(Date::class.java, DateTypeAdapter())
    .registerTypeAdapter(RemoteDevice::class.java, DeviceTypeAdapter())
    .create()

private val retrofit = Retrofit.Builder()
    .baseUrl(API_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(okHttpClient)
    .build()

object RetrofitClient {
    val homeService: HomeService by lazy {
        retrofit.create(HomeService::class.java)
    }

    val roomService: RoomService by lazy {
        retrofit.create(RoomService::class.java)
    }

    val deviceService: DeviceService by lazy {
        retrofit.create(DeviceService::class.java)
    }

    val routineService: RoutineService by lazy {
        retrofit.create(RoutineService::class.java)
    }
}