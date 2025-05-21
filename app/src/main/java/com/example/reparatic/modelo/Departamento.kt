package com.example.reparatic.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Departamento(
    @SerialName(value = "codDpto")
    val codDpto: Int,

    @SerialName(value = "nombreDpto")
    val nombreDpto: String
)




