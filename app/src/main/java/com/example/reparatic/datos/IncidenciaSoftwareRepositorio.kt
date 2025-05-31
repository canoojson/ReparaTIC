package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.IncidenciaSoftware
import retrofit2.Response

interface IncidenciaSoftwareRepositorio {
    suspend fun obtener(): List<IncidenciaSoftware>
    suspend fun insertar(incidencia: IncidenciaSoftware): IncidenciaSoftware
    suspend fun actualizar(id: Int, incidencia: IncidenciaSoftware): Response<IncidenciaSoftware>
    suspend fun eliminar(id: Int): Response<IncidenciaSoftware>
}

class ConexionIncidenciaSoftwareRepositorio(private val api: Api) : IncidenciaSoftwareRepositorio {
    override suspend fun obtener(): List<IncidenciaSoftware> = api.obtenerIncidenciasSoftware()
    override suspend fun insertar(incidencia: IncidenciaSoftware): IncidenciaSoftware = api.insertarIncidenciaSoftware(incidencia)
    override suspend fun actualizar(id: Int, incidencia: IncidenciaSoftware): Response<IncidenciaSoftware> = api.actualizarIncidenciaSoftware(id, incidencia)
    override suspend fun eliminar(id: Int): Response<IncidenciaSoftware> = api.eliminarIncidenciaSoftware(id)
}