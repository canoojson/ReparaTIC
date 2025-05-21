package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Estado(
    @SerialName(value = "idEstado")
    val idEstado: Int,

    @SerialName(value = "descrip")
    var descrip: String
)