package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidenciaSoftware(
    @SerialName("SO")
    val SO: String,

    @SerialName("software")
    val software: String,

    @SerialName("clave")
    val clave: String
)
