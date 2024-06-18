package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice

abstract class Device(
    val id: String?,
    val name: String,
    val room: Room?,
    val type: DeviceType
) {
    abstract fun asRemoteModel(): RemoteDevice<*>
}