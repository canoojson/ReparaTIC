package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.Estado
import retrofit2.Response

interface EstadoRepositorio {
    suspend fun obtenerEstados(): List<Estado>
    suspend fun insertarEstado(estado: Estado): Estado
    suspend fun actualizarEstado(id: Int, estado: Estado): Response<Estado>
    suspend fun eliminarEstado(id: Int): Response<Estado>
}

class ConexionEstadoRepositorio(
    private val api: Api
) : EstadoRepositorio {

    override suspend fun obtenerEstados(): List<Estado> = api.obtenerEstados()

    override suspend fun insertarEstado(estado: Estado): Estado = api.insertarEstado(estado)

    override suspend fun actualizarEstado(id: Int, estado: Estado): Response<Estado> = api.actualizarEstado(id, estado)

    override suspend fun eliminarEstado(id: Int): Response<Estado> = api.eliminarEstado(id)
}
