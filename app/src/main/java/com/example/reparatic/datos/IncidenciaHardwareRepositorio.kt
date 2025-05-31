package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.IncidenciaHardware
import retrofit2.Response

interface IncidenciaHardwareRepositorio {
    suspend fun obtener(): List<IncidenciaHardware>
    suspend fun insertar(incidencia: IncidenciaHardware): IncidenciaHardware
    suspend fun actualizar(id: Int, incidencia: IncidenciaHardware): Response<IncidenciaHardware>
    suspend fun eliminar(id: Int): Response<IncidenciaHardware>
}

class ConexionIncidenciaHardwareRepositorio(private val api: Api): IncidenciaHardwareRepositorio {
    override suspend fun obtener(): List<IncidenciaHardware> = api.obtenerIncidenciasHardware()
    override suspend fun insertar(incidencia: IncidenciaHardware): IncidenciaHardware = api.insertarIncidenciaHardware(incidencia)
    override suspend fun actualizar(id: Int, incidencia: IncidenciaHardware): Response<IncidenciaHardware> = api.actualizarIncidenciaHardware(id, incidencia)
    override suspend fun eliminar(id: Int): Response<IncidenciaHardware> = api.eliminarIncidenciaHardware(id)
}