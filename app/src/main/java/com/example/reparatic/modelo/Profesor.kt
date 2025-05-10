package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profesor(
    @SerialName("IdProfesor")
    val IdProfesor: Int,
    @SerialName("dni")
    val dni: String,
    @SerialName("Nombre")
    val nombre: String,
    @SerialName("Apellidos")
    val apellidos: String,
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("departamento")
    val departamento: Departamento,
    @SerialName("email")
    val email:String,
    @SerialName("pwd")
    val pwd: String,
    @SerialName("rol")
    val rol: Rol,
    @SerialName("username")
    val username: String
)
