package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.model.DeviceType
import ar.edu.itba.harmony_mobile.remote.model.RemoteDevice

abstract class Device(
    val id: String?,
    val name: String,
    val type: DeviceType
) {
    abstract fun asRemoteModel(): RemoteDevice<*>
}