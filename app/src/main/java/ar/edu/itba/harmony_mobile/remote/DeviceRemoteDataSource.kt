package ar.edu.itba.harmony_mobile.remote

import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.remote.api.DeviceService
import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHomeMeta
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoomMeta
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach

class DeviceRemoteDataSource(
    private val deviceService: DeviceService
) : RemoteDataSource() {

    val devices: Flow<List<RemoteDevice<*>>> = flow {
        while (true) {
            val devices = handleApiResponse {
                deviceService.getDevices()
            }
            updateDevicesByRoomByHome()
            emit(devices)
            delay(DELAY)
        }
    }

    val personalDevicesHome: RemoteHome = RemoteHome()

    val personalDevicesRoom: RemoteRoom = RemoteRoom()

    var devicesByRoomByHome: MutableSet<RemoteHome> = HashSet();

    init {

        val metaHome = RemoteHomeMeta()
        metaHome.size = "0"
        metaHome.color = "0"

        val model = RemoteHome()
        model.id = "0"
        model.name = "Personal Devices"
        model.rooms = hashSetOf(personalDevicesRoom)
        model.meta = metaHome

        val metaRoom: RemoteRoomMeta = RemoteRoomMeta()
        metaRoom.size = "0"
        metaRoom.color = "0"

        personalDevicesRoom.id = "0"
        personalDevicesRoom.name = "Personal Devices"
        personalDevicesRoom.devices = HashSet()
        personalDevicesRoom.home = personalDevicesHome
        personalDevicesRoom.meta = metaRoom
    }

    private suspend fun updateDevicesByRoomByHome() {
        devicesByRoomByHome = HashSet()
        devices.collect {
            it.forEach { jt ->
                val h: RemoteHome;
                val r: RemoteRoom;
                if (jt.room?.home == null) {
                    jt.room = personalDevicesRoom
                    h = personalDevicesHome
                    r = personalDevicesRoom
                } else {
                    h = jt.room!!.home!!
                    r = jt.room!!
                }
                devicesByRoomByHome.add(h) // won't add if already present
                h.rooms.add(r) // won't add if already present
                r.devices.add(jt) // by definition, will not be present
            }
        }
    }


    suspend fun getDevice(deviceId: String): RemoteDevice<*> {
        return handleApiResponse {
            deviceService.getDevice(deviceId)
        }
    }

    suspend fun addDevice(device: RemoteDevice<*>): RemoteDevice<*> {
        return handleApiResponse {
            deviceService.addDevice(device)
        }
    }

    suspend fun modifyDevice(device: RemoteDevice<*>): Boolean {
        return handleApiResponse {
            deviceService.modifyDevice(device.id!!, device)
        }
    }

    suspend fun deleteDevice(deviceId: String): Boolean {
        return handleApiResponse {
            deviceService.deleteDevice(deviceId)
        }
    }

    suspend fun executeDeviceAction(
        deviceId: String,
        action: String,
        parameters: Array<*>
    ): Array<*> {
        return handleApiResponse {
            deviceService.executeDeviceAction(deviceId, action, parameters)
        }
    }

    companion object {
        const val DELAY: Long = 10000
    }
}