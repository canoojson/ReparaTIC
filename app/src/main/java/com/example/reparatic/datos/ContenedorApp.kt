package com.example.reparatic.datos

import android.content.Context
import com.example.reparatic.conexion.Api
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface ContenedorApp{
    val loginRepositorio: LoginRepositorio
    val incidenciaRepositorio: IncidenciaRepositorio
    val profesorRepositorio: ProfesorRepositorio
    val departamentoRepositorio: DepartamentoRepositorio
    val estadoRepositorio: EstadoRepositorio
    val ubicacionRepositorio: UbicacionRepositorio
    val tiposHwRepositorio: TiposHwRepositorio
}

class IncidenciaContenedorApp(private val context: Context): ContenedorApp{
    private val baseUrl = "http://192.168.1.14:8080/api/"

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val servicioretrofitLogin: Api by lazy {
        retrofit.create(Api::class.java)
    }

    private val servicioretrofitIncidencias: Api by lazy {
        retrofit.create(Api::class.java)
    }

    private val servicioretrofitProfesores: Api by lazy {
        retrofit.create(Api::class.java)
    }

    private val servicioretrofitDepartamentos: Api by lazy {
        retrofit.create(Api::class.java)
    }

    private val servicioretrofitEstados: Api by lazy {
        retrofit.create(Api::class.java)
    }
    private val servicioretrofitUbicaciones: Api by lazy {
        retrofit.create(Api::class.java)
    }
    private val servicioretrofitTiposHW: Api by lazy {
        retrofit.create(Api::class.java)
    }

    override val loginRepositorio: LoginRepositorio by lazy {
        ConexionLoginRepositorio(servicioretrofitLogin)
    }

    override val incidenciaRepositorio: IncidenciaRepositorio by lazy {
        ConexionIncidenciaRepositorio(servicioretrofitIncidencias)
    }

    override val profesorRepositorio: ProfesorRepositorio by lazy {
        ConexionProfesorRepositorio(servicioretrofitProfesores)
    }

    override val departamentoRepositorio: DepartamentoRepositorio by lazy {
        conexionDepartamentoRepositorio(servicioretrofitDepartamentos)
    }

    override val estadoRepositorio: EstadoRepositorio by lazy {
        ConexionEstadoRepositorio(servicioretrofitEstados)
    }
    override val ubicacionRepositorio: UbicacionRepositorio by lazy {
        ConexionUbicacionRepositorio(servicioretrofitUbicaciones)
    }
    override val tiposHwRepositorio: TiposHwRepositorio by lazy {
        ConexionTiposHwRepositorio(servicioretrofitTiposHW)
    }

}