package com.example.reparatic.modelo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Ubicacion(
    @SerialName(value = "IdUbicacion")
    val idUbicacion: Int? = null,

    @SerialName(value = "Nombre")
    val nombre: String,

    @SerialName(value = "Descrip")
    val descrip: String
)
