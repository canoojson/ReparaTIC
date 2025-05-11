package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TiposHw(
    @SerialName("idTipoHw")
    val idTipoHw: Int? = null,

    @SerialName("descrip")
    val descrip: String,

    @SerialName("incidenciasHardware")
    val incidenciasHardware: List<IncidenciaHardware> = emptyList()
)
