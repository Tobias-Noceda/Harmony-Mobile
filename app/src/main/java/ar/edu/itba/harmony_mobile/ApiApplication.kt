package ar.edu.itba.harmony_mobile

import android.app.Application
import ar.edu.itba.harmony_mobile.remote.DeviceRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.RoomRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.api.RetrofitClient
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.remote.repository.RoomRepository

class ApiApplication  : Application() {

    private val roomRemoteDataSource: RoomRemoteDataSource
        get() = RoomRemoteDataSource(RetrofitClient.roomService)

    private val deviceRemoteDataSource: DeviceRemoteDataSource
        get() = DeviceRemoteDataSource(RetrofitClient.deviceService)

    val roomRepository: RoomRepository
        get() = RoomRepository(roomRemoteDataSource)

    val deviceRepository: DeviceRepository
        get() = DeviceRepository(deviceRemoteDataSource)
}