package ar.edu.itba.harmony_mobile.remote

import ar.edu.itba.harmony_mobile.remote.api.HomeService
import ar.edu.itba.harmony_mobile.remote.model.RemoteHome
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRemoteDataSource(
    private val homeService: HomeService
) : RemoteDataSource() {

    val homes: Flow<List<RemoteHome>> = flow {
        while (true) {
            val devices = handleApiResponse {
                homeService.getHomes()
            }
            emit(devices)
            delay(DELAY)
        }
    }

    suspend fun getHome(deviceId: String): RemoteHome {
        return handleApiResponse {
            homeService.getHome(deviceId)
        }
    }

    suspend fun addHome(device: RemoteHome): RemoteHome {
        return handleApiResponse {
            homeService.addHome(device)
        }
    }

    suspend fun modifyHome(device: RemoteHome): Boolean {
        return handleApiResponse {
            homeService.modifyHome(device.id!!, device)
        }
    }

    suspend fun deleteHome(deviceId: String): Boolean {
        return handleApiResponse {
            homeService.deleteHome(deviceId)
        }
    }

    companion object {
        const val DELAY: Long = 10000
    }
}