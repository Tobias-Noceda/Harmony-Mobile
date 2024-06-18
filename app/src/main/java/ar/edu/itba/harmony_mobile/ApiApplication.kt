package ar.edu.itba.harmony_mobile

import android.app.Application
import ar.edu.itba.harmony_mobile.remote.DeviceRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.HomeRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.RoomRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.RoutineRemoteDataSource
import ar.edu.itba.harmony_mobile.remote.api.RetrofitClient
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.repository.HomeRepository
import ar.edu.itba.harmony_mobile.repository.RoomRepository
import ar.edu.itba.harmony_mobile.repository.RoutineRepository

class ApiApplication : Application() {

    private val roomRemoteDataSource: RoomRemoteDataSource
        get() = RoomRemoteDataSource(RetrofitClient.roomService)

    private val deviceRemoteDataSource: DeviceRemoteDataSource
        get() = DeviceRemoteDataSource(RetrofitClient.deviceService)

    private val homeRemoteDataSource: HomeRemoteDataSource
        get() = HomeRemoteDataSource(RetrofitClient.homeService)

    private val routineRemoteDataSource: RoutineRemoteDataSource
        get() = RoutineRemoteDataSource(RetrofitClient.routineService)

    val roomRepository: RoomRepository
        get() = RoomRepository(roomRemoteDataSource)

    val deviceRepository: DeviceRepository
        get() = DeviceRepository(deviceRemoteDataSource)

    val homeRepository: HomeRepository
        get() = HomeRepository(homeRemoteDataSource)

    val routineRepository: RoutineRepository
        get() = RoutineRepository(routineRemoteDataSource)
}