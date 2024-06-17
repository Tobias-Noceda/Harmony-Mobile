package ar.edu.itba.example.api.remote.model

import com.google.gson.annotations.SerializedName

class RemoteError {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("description")
    lateinit var description: List<String>
}
