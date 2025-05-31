package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidenciaHardware(
    @SerialName(value = "idh")
    val idh: Int,

    @SerialName("modelo")
    var modelo: String,

    @SerialName("num_serie")
    var numSerie: String,

    @SerialName("tipoHw")
    var tipoHw: TiposHw?
)
