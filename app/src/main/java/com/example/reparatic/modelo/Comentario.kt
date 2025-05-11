package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comentario(
    @SerialName(value = "IdComentario")
    val idComentario: Int? = null,

    @SerialName(value = "Comentario")
    val comentario: String,

    @SerialName(value = "incidencia")
    val incidencia: Incidencia
)
