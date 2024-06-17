package ar.edu.itba.harmony_mobile.remote.api

import ar.edu.itba.harmony_mobile.remote.model.RemoteBlinds
import ar.edu.itba.harmony_mobile.remote.model.RemoteDevice
import ar.edu.itba.harmony_mobile.remote.model.RemoteDeviceType
import ar.edu.itba.harmony_mobile.remote.model.RemoteDoor
import ar.edu.itba.harmony_mobile.remote.model.RemoteLamp
import ar.edu.itba.harmony_mobile.remote.model.RemoteRefrigerator
import ar.edu.itba.harmony_mobile.remote.model.RemoteSprinkler
import ar.edu.itba.harmony_mobile.remote.model.RemoteVacuum
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DeviceTypeAdapter : JsonDeserializer<RemoteDevice<*>?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): RemoteDevice<*>? {
        val gson = Gson()
        val jsonDeviceObject = json.asJsonObject
        val jsonDeviceTypeObject = jsonDeviceObject["type"].asJsonObject
        val deviceTypeId = jsonDeviceTypeObject["id"].asString
        return if (deviceTypeId == RemoteDeviceType.LAMP_DEVICE_TYPE_ID) {
            gson.fromJson(jsonDeviceObject, object : TypeToken<RemoteLamp?>() {}.type)
        } else if (deviceTypeId == RemoteDeviceType.REFRIGERATOR_DEVICE_TYPE_ID) {
            gson.fromJson(jsonDeviceObject, object : TypeToken<RemoteRefrigerator?>() {}.type)
        } else if (deviceTypeId == RemoteDeviceType.BLINDS_DEVICE_TYPE_ID) {
            gson.fromJson(jsonDeviceObject, object : TypeToken<RemoteBlinds?>() {}.type)
        } else if (deviceTypeId == RemoteDeviceType.VACUUM_DEVICE_TYPE_ID) {
            gson.fromJson(jsonDeviceObject, object : TypeToken<RemoteVacuum?>() {}.type)
        } else if (deviceTypeId == RemoteDeviceType.DOOR_DEVICE_TYPE_ID) {
            gson.fromJson(jsonDeviceObject, object : TypeToken<RemoteDoor?>() {}.type)
        } else if (deviceTypeId == RemoteDeviceType.SPRINKLER_DEVICE_TYPE_ID) {
            gson.fromJson(jsonDeviceObject, object : TypeToken<RemoteSprinkler?>() {}.type)
        } else null
    }
}