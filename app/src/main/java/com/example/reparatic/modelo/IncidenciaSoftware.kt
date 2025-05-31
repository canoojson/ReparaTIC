package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidenciaSoftware(
    @SerialName(value = "ids")
    val ids: Int,

    @SerialName("so")
    var SO: String,

    @SerialName("software")
    var software: String,

    @SerialName("clave")
    var clave: String
)
