package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profesor(
    @SerialName("dni")
    val dni: String,
    @SerialName("departamento")
    val departamento: Departamento?,
    @SerialName("email")
    val email:String,
    @SerialName("pwd")
    val pwd: String,
    @SerialName("rol")
    val rol: Rol?,
    @SerialName("username")
    val username: String,
    @SerialName("nombre")
    val nombre: String,
    @SerialName("apellidos")
    val apellidos: String,
    @SerialName("idProfesor")
    val idProfesor: Int
)
