package ar.edu.itba.harmony_mobile.remote

import ar.edu.itba.harmony_mobile.remote.api.RoomService
import ar.edu.itba.harmony_mobile.remote.model.RemoteRoom

class RoomRemoteDataSource(
    private val roomService: RoomService
) : RemoteDataSource() {

    suspend fun getRooms(): List<RemoteRoom> {
        return handleApiResponse {
            roomService.getRooms()
        }
    }

    suspend fun getRoom(roomId: String): RemoteRoom {
        return handleApiResponse {
            roomService.getRoom(roomId)
        }
    }

    suspend fun addRoom(room: RemoteRoom): RemoteRoom {
        return handleApiResponse {
            roomService.addRoom(room)
        }
    }

    suspend fun modifyRoom(room: RemoteRoom): Boolean {
        return handleApiResponse {
            roomService.modifyRoom(room.id!!, room)
        }
    }

    suspend fun deleteRoom(roomId: String): Boolean {
        return handleApiResponse {
            roomService.deleteRoom(roomId)
        }
    }
}