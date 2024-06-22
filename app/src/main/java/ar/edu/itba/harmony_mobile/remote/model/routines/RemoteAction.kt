package ar.edu.itba.harmony_mobile.remote.model.routines;

import ar.edu.itba.harmony_mobile.model.Action
import com.google.gson.annotations.SerializedName

class RemoteAction {
    @SerializedName("meta")
    var meta: RemoteActionMeta = RemoteActionMeta()

    @SerializedName("device")
    var device: AuxDevice = AuxDevice()

    @SerializedName("actionName")
    var actionName: String = ""

    @SerializedName("params")
    var params: List<String> = ArrayList()


    fun asModel(): Action {
        return Action(deviceName = device.name, deviceId = device.id,actionName = actionName, params = params)
    }

    override fun toString(): String {
        return "{RemoteAction;name:${actionName};params:${params};device:${device}}"
    }

    class AuxDevice {
        @SerializedName("name")
        var name: String = ""

        @SerializedName("id")
        var id: String = ""
    }
}
