package ar.edu.itba.harmony_mobile.remote.repository

import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.remote.RoomRemoteDataSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RoomRepository(
    private val remoteDataSource: RoomRemoteDataSource
) {
    private val roomsMutex = Mutex()
    private var rooms: List<Room> = emptyList()

    private suspend fun updateCache(rooms: List<Room>) {
        roomsMutex.withLock {
            this.rooms = rooms
        }
    }

    suspend fun getRooms(refresh: Boolean = false): List<Room> {
        if (refresh || rooms.isEmpty()) {
            val result = remoteDataSource.getRooms()
            updateCache(result.map { it.asModel() })
        }

        return roomsMutex.withLock { this.rooms }
    }

    suspend fun getRoom(roomId: String): Room {
        return remoteDataSource.getRoom(roomId).asModel()
    }

    suspend fun addRoom(room: Room): Room {
        val newRoom = remoteDataSource.addRoom(room.asRemoteModel()).asModel()
        updateCache(emptyList())
        return newRoom
    }

    suspend fun modifyRoom(room: Room): Boolean {
        val result = remoteDataSource.modifyRoom(room.asRemoteModel())
        updateCache(emptyList())
        return result
    }

    suspend fun deleteRoom(roomId: String): Boolean {
        val result = remoteDataSource.deleteRoom(roomId)
        updateCache(emptyList())
        return result
    }
}