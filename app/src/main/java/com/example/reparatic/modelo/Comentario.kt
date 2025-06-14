package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comentario(
    @SerialName(value = "idComentario")
    val idComentario: Int = 0,

    @SerialName(value = "comentario")
    val comentario: String?,

    @SerialName(value = "fecha_comentario")
    val fecha: String?,

    @SerialName(value = "profesor")
    val profesor: Profesor?
)
