package com.example.reparatic.conexion

import com.example.reparatic.modelo.Departamento
import com.example.reparatic.modelo.Estado
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.LoginRequest
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.TiposHw
import com.example.reparatic.modelo.Ubicacion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {
    //LOGIN
    @POST("login")
    suspend fun  login(
        @Body loginRequest: LoginRequest
    ): Profesor

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

    @GET("profesores/departamento/{nombre}")
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

    //DEPARTAMENTOS
    @GET("departamentos")
    suspend fun obtenerDepartamentos(): List<Departamento>

    @POST("departamentos")
    suspend fun insertarDepartamento(
        @Body departamento: Departamento
    ):Departamento

    @PUT("departamentos/{id}")
    suspend fun actualizarDepartamento(
        @Path("id") id: Int,
        @Body departamento: Departamento
    ): Response<Departamento>

    @DELETE("departamentos/{id}")
    suspend fun eliminarDepartamento(
        @Path("id") id: Int
    ): Response<Departamento>

    // ESTADOS
    @GET("estados")
    suspend fun obtenerEstados(): List<Estado>

    @POST("estados")
    suspend fun insertarEstado(
        @Body estado: Estado
    ): Estado

    @PUT("estados/{id}")
    suspend fun actualizarEstado(
        @Path("id") id: Int,
        @Body estado: Estado
    ): Response<Estado>

    @DELETE("estados/{id}")
    suspend fun eliminarEstado(
        @Path("id") id: Int
    ): Response<Estado>
    // UBICACIONES
    @GET("ubicaciones")
    suspend fun obtenerUbicaciones(): List<Ubicacion>

    @POST("ubicaciones")
    suspend fun insertarUbicacion(
        @Body ubicacion: Ubicacion
    ): Ubicacion

    @PUT("ubicaciones/{id}")
    suspend fun actualizarUbicacion(
        @Path("id") id: Int,
        @Body ubicacion: Ubicacion
    ): Response<Ubicacion>

    @DELETE("ubicaciones/{id}")
    suspend fun eliminarUbicacion(
        @Path("id") id: Int
    ): Response<Ubicacion>
    //TIPO INCIDENCIAS

    @GET("tiposhw")
    suspend fun obtenerTiposHW(): List<TiposHw>

    @POST("tiposhw")
    suspend fun insertarTipoHW(
        @Body tiposHw: TiposHw
    ): TiposHw

    @PUT("tiposhw/{id}")
    suspend fun actualizarTipoHW(
        @Path("id") id: Int,
        @Body tiposHw: TiposHw
    ): Response<TiposHw>

    @DELETE("tiposhw/{id}")
    suspend fun eliminarTipoHW(
        @Path("id") id: Int
    ): Response<TiposHw>
}