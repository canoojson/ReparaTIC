package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Permiso(
    @SerialName(value = "codPermiso")
    val codPermiso: Int,

    @SerialName(value = "descrip")
    val descrip: String
)