package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidenciaSoftware(
    @SerialName("so")
    var SO: String,

    @SerialName("software")
    var software: String,

    @SerialName("clave")
    var clave: String
)
