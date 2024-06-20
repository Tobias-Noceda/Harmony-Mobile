package ar.edu.itba.harmony_mobile.remote

import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHome
import ar.edu.itba.harmony_mobile.remote.model.homes.RemoteHomeMeta
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoom
import ar.edu.itba.harmony_mobile.remote.model.rooms.RemoteRoomMeta

class GlobalDataHomes {
    companion object {
        private fun producePersonalDevicesRoom(): RemoteRoom {
            val auxRoom: RemoteRoom = RemoteRoom()
            val auxHome: RemoteHome = RemoteHome()
            val metaHome = RemoteHomeMeta()
            metaHome.size = "0"
            metaHome.color = "0"

            auxHome.id = "0"
            auxHome.name = "Personal Devices"
            auxHome.rooms = hashSetOf(auxRoom)
            auxHome.meta = metaHome
            auxHome.rooms = personalRooms

            val metaRoom: RemoteRoomMeta = RemoteRoomMeta()
            metaRoom.size = "0"
            metaRoom.color = "0"

            auxRoom.id = "0"
            auxRoom.name = "Personal Devices"
            auxRoom.devices = HashSet()
            auxRoom.home = auxHome
            auxRoom.meta = metaRoom
            auxRoom.devices = personalDevices
            return auxRoom
        }

        private fun producePersonalDevicesHome(): RemoteHome {
            val auxRoom: RemoteRoom = RemoteRoom()
            val auxHome: RemoteHome = RemoteHome()
            val metaHome = RemoteHomeMeta()
            metaHome.size = "0"
            metaHome.color = "0"

            auxHome.id = "0"
            auxHome.name = "Personal Devices"
            auxHome.meta = metaHome
            //personalRooms.add(auxRoom) // Currently, the HarmonyApp.kt screen flips its shit if I initialize this
            auxHome.rooms = personalRooms

            val metaRoom: RemoteRoomMeta = RemoteRoomMeta()
            metaRoom.size = "0"
            metaRoom.color = "0"

            auxRoom.id = "0"
            auxRoom.name = "Personal Devices"
            auxRoom.devices = HashSet()
            auxRoom.home = auxHome
            auxRoom.meta = metaRoom
            auxRoom.devices = personalDevices
            return auxHome
        }

        val personalDevicesHome: RemoteHome
            get() = producePersonalDevicesHome()

        val personalDevicesRoom: RemoteRoom
            get() = producePersonalDevicesRoom()


        private val personalRooms: MutableSet<RemoteRoom> = HashSet()
        private val personalDevices: MutableSet<RemoteDevice<*>> = HashSet()
    }
}