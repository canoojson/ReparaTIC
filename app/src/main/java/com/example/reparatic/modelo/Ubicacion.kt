package com.example.reparatic.modelo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Ubicacion(
    @SerialName(value = "idUbicacion")
    val idUbicacion: Int,

    @SerialName(value = "nombre")
    var nombre: String,

    @SerialName(value = "descrip")
    var descrip: String
)
