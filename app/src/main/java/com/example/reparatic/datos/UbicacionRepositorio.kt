package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.Ubicacion
import retrofit2.Response

interface UbicacionRepositorio {
    suspend fun obtenerUbicaciones(): List<Ubicacion>
    suspend fun insertarUbicacion(ubicacion: Ubicacion): Ubicacion
    suspend fun actualizarUbicacion(id: Int, ubicacion: Ubicacion): Response<Ubicacion>
    suspend fun eliminarUbicacion(id: Int): Response<Ubicacion>
}

class ConexionUbicacionRepositorio(
    private val api: Api
) : UbicacionRepositorio {
    override suspend fun obtenerUbicaciones(): List<Ubicacion> = api.obtenerUbicaciones()
    override suspend fun insertarUbicacion(ubicacion: Ubicacion): Ubicacion = api.insertarUbicacion(ubicacion)
    override suspend fun actualizarUbicacion(id: Int, ubicacion: Ubicacion): Response<Ubicacion> = api.actualizarUbicacion(id, ubicacion)
    override suspend fun eliminarUbicacion(id: Int): Response<Ubicacion> = api.eliminarUbicacion(id)
}
