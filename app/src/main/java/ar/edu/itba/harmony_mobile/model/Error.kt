package ar.edu.itba.harmony_mobile.model

data class Error(
    val code: Int?,
    val message: String,
    val description: List<String>? = null
)