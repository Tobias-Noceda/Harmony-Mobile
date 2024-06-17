package ar.edu.itba.harmony_mobile

class DataSourceException(
    var code: Int,
    message: String,
    var details: List<String>?
) : Exception(message)