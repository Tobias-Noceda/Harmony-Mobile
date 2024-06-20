package ar.edu.itba.harmony_mobile.model

import ar.edu.itba.harmony_mobile.remote.model.devices.RemoteDevice

abstract class Device(
    val id: String?,
    val name: String,
    val room: Room?,
    val type: DeviceType
) {
    abstract fun asRemoteModel(): RemoteDevice<*>
    override fun toString(): String {
        return "{Device;id:${id};name:${name};Room:${room?.name}"
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}