package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TiposHw(
    @SerialName("idTipoHw")
    val idTipoHw: Int,

    @SerialName("descrip")
    var descrip: String
)
