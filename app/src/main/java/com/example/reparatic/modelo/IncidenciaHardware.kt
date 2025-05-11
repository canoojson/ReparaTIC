package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidenciaHardware(
    @SerialName("modelo")
    val modelo: String,

    @SerialName("num_serie")
    val numSerie: String,

    @SerialName("tipoHw")
    val tipoHw: TiposHw
)
