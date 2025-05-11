package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rol(
    @SerialName(value = "idRol")
    val idRol: Int? = null,

    @SerialName(value = "descrip")
    val descrip: String,

    @SerialName(value = "permisos")
    val permisos: List<Permiso>
)
