package ar.edu.itba.harmony_mobile.remote.model

import com.google.gson.annotations.SerializedName

class RemoteDeviceType {
    @SerializedName("id")
    lateinit var id: String

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("powerUsage")
    var powerUsage: Int? = null

    companion object {
        const val LAMP_DEVICE_TYPE_ID = "go46xmbqeomjrsjr"
        const val SPRINKLER_DEVICE_TYPE_ID = "dbrlsh7o5sn8ur4i"
        const val BLINDS_DEVICE_TYPE_ID = "eu0v2xgprrhhg41g"
        const val REFRIGERATOR_DEVICE_TYPE_ID = "rnizejqr2di0okho"
        const val VACUUM_DEVICE_TYPE_ID = "ofglvd9gqx8yfl3l"
        const val DOOR_DEVICE_TYPE_ID = "lsf78ly0eqrjbz91"
    }
}