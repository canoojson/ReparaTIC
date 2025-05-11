package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Estado(
    @SerialName(value = "IdEstado")
    val idEstado: Int? = null,

    @SerialName(value = "Descrip")
    val descrip: String
)