package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.Incidencia
import retrofit2.Response

interface IncidenciaRepositorio{
    suspend fun obtenerIncidencias(): List<Incidencia>
    suspend fun insertarIncidencia(incidencia: Incidencia): Incidencia
    suspend fun actualizarIncidencia(id: Int, incidencia: Incidencia): Response<Incidencia>
    suspend fun eliminarIncidencia(id: Int): Response<Incidencia>
}

class ConexionIncidenciaRepositorio(
    private val api:Api
): IncidenciaRepositorio{
    override suspend fun obtenerIncidencias():List<Incidencia> = api.obtenerIncidencias()
    override suspend fun insertarIncidencia(incidencia: Incidencia): Incidencia = api.insertarIncidencia(incidencia)
    override suspend fun actualizarIncidencia(id: Int, incidencia: Incidencia): Response<Incidencia> = api.actualizarIncidencia(id, incidencia)
    override suspend fun eliminarIncidencia(id: Int): Response<Incidencia> = api.eliminarIncidencia(id)
}


