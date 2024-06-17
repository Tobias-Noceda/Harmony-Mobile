package ar.edu.itba.harmony_mobile.repository

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.remote.RoomRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepository(
    private val remoteDataSource: RoomRemoteDataSource
) {
    val rooms: Flow<List<Room>> = remoteDataSource.rooms
        .map { it.map { jt -> jt.asModel() } }


    suspend fun getRoom(roomId: String): Room {
        return remoteDataSource.getRoom(roomId).asModel()
    }

    suspend fun addRoom(room: Room): Room {
        return remoteDataSource.addRoom(room.asRemoteModel()).asModel()
    }

    suspend fun modifyRoom(room: Room): Boolean {
        return remoteDataSource.modifyRoom(room.asRemoteModel())
    }

    suspend fun deleteRoom(roomId: String): Boolean {
        return remoteDataSource.deleteRoom(roomId)
    }
}