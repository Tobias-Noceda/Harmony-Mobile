package ar.edu.itba.harmony_mobile.repository

import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.remote.HomeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepository(
    private val remoteDataSource: HomeRemoteDataSource
) {
    val homes: Flow<List<Home>> = remoteDataSource.homes
        .map { it.map { jt -> jt.asModel() } }


    suspend fun getHome(homeId: String): Home {
        return remoteDataSource.getHome(homeId).asModel()
    }

    suspend fun addHome(home: Home): Home {
        return remoteDataSource.addHome(home.asRemoteModel()).asModel()
    }

    suspend fun modifyHome(home: Home): Boolean {
        return remoteDataSource.modifyHome(home.asRemoteModel())
    }

    suspend fun deleteHome(homeId: String): Boolean {
        return remoteDataSource.deleteHome(homeId)
    }
}