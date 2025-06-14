package com.example.reparatic.modelo

import com.example.reparatic.Serializers.ByteArrayBase64Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Incidencia(
    @SerialName(value = "idIncidencia")
    val idIncidencia: Int,
    @SerialName(value = "tipo")
    val tipo: String,
    @SerialName(value="fecha_incidencia")
    val fecha_incidencia: String?,
    @SerialName(value="fecha_introduccion")
    val fecha_introduccion: String,
    @SerialName(value = "profesor")
    val profesor: Profesor?,
    @SerialName(value = "departamento")
    val departamento: Departamento?,
    @SerialName(value = "ubicacion")
    val ubicacion: Ubicacion?,
    @SerialName(value = "descripcion")
    val descripcion: String,
    @SerialName(value = "observaciones")
    val observaciones: String?,
    @SerialName(value = "estado")
    val estado: Estado?,
    @SerialName(value="responsable")
    val responsable: Profesor?,
    @SerialName("fecha_resolucion")
    val fecha_resolucion: String?,
    @SerialName("tiempo_invertido")
    val tiempo_invertido: String?,
    @Serializable(with = ByteArrayBase64Serializer::class)
    val mas_info: ByteArray?,
    @SerialName("incidenciaHardware")
    val incidenciaHardware: IncidenciaHardware?,
    @SerialName("incidenciaSoftware")
    val incidenciaSoftware: IncidenciaSoftware?,
    @SerialName("comentarios")
    val comentarios: List<Comentario>?
)
