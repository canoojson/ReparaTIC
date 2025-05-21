package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.ui.IncidenciaUIState
import retrofit2.Response

interface ProfesorRepositorio {
    suspend fun obtenerProfesores(): List<Profesor>
    suspend fun obtenerProfesoresDepartamento(nombre: String): List<Profesor>
    suspend fun insertarProfesor(profesor: Profesor): Profesor
    suspend fun actualizarProfesor(id: Int, profesor: Profesor): Response<Profesor>
    suspend fun eliminarProfesor(id: Int): Response<Profesor>
}

class ConexionProfesorRepositorio(
    private val api: Api
): ProfesorRepositorio {
    override suspend fun obtenerProfesores(): List<Profesor> = api.obtenerProfesores()
    override suspend fun obtenerProfesoresDepartamento(nombre: String): List<Profesor> = api.obtenerProfesoresDepartamento(nombre)
    override suspend fun insertarProfesor(profesor: Profesor): Profesor = api.insertarProfesor(profesor)
    override suspend fun actualizarProfesor(id: Int, profesor: Profesor): Response<Profesor> = api.actualizarProfesor(id, profesor)
    override suspend fun eliminarProfesor(id: Int): Response<Profesor> = api.eliminarProfesor(id)
}