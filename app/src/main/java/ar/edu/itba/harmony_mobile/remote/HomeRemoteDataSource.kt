package ar.edu.itba.harmony_mobile.remote

import ar.edu.itba.harmony_mobile.remote.api.HomeService
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRemoteDataSource(
    private val homeService: HomeService
) : RemoteDataSource() {

    val homes: Flow<List<RemoteHome>> = flow {
        while (true) {
            val homes = handleApiResponse {
                homeService.getHomes()
            }
            emit(homes)
            delay(DELAY)
        }
    }

    suspend fun getHome(homeId: String): RemoteHome {
        return handleApiResponse {
            homeService.getHome(homeId)
        }
    }

    suspend fun addHome(home: RemoteHome): RemoteHome {
        return handleApiResponse {
            homeService.addHome(home)
        }
    }

    suspend fun modifyHome(home: RemoteHome): Boolean {
        return handleApiResponse {
            homeService.modifyHome(home.id!!, home)
        }
    }

    suspend fun deleteHome(homeId: String): Boolean {
        return handleApiResponse {
            homeService.deleteHome(homeId)
        }
    }

    companion object {
        const val DELAY: Long = 10000
    }
}