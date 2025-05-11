package com.example.reparatic.conexion

import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {
    //INCIDENCIAS
    @GET("incidencias")
    suspend fun obtenerIncidencias(): List<Incidencia>

    @POST("incidencias")
    suspend fun insertarIncidencia(
        @Body incidencia: Incidencia
    ):Incidencia

    @PUT("incidencias/{id}")
    suspend fun actualizarIncidencia(
        @Path("id") id: Int,
        @Body incidencia: Incidencia
    ): Response<Incidencia>

    @DELETE("incidencias/{id}")
    suspend fun eliminarIncidencia(
        @Path("id") id: Int
    ): Response<Incidencia>

    //PROFESORES

    @GET("profesores")
    suspend fun obtenerProfesores(): List<Profesor>

    @GET("profesores/departamenti/{nombre}")
    suspend fun obtenerProfesoresDepartamento(
        @Path("nombre") nombre: String
    ): List<Profesor>

    @POST("profesores")
    suspend fun insertarProfesor(
        @Body profesor: Profesor
    ): Profesor

    @PUT("profesores/{id}")
    suspend fun actualizarProfesor(
        @Path("id") id: Int,
        @Body profesor: Profesor
    ): Response<Profesor>

    @DELETE("profesores/{id}")
    suspend fun eliminarProfesor(
        @Path("id") id: Int
    ): Response<Profesor>
}