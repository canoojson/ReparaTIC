package com.example.reparatic.datos

import android.content.Context
import com.example.reparatic.conexion.Api
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface ContenedorApp{
    val incidenciaRepositorio: IncidenciaRepositorio
}

class IncidenciaContenedorApp(private val context: Context): ContenedorApp{
    private val baseUrl = "http://192.168.1.31:8000/api/"

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val servicioretrofitIncidencias: Api by lazy {
        retrofit.create(Api::class.java)
    }

    override val incidenciaRepositorio: IncidenciaRepositorio by lazy {
        ConexionIncidenciaRepositorio(servicioretrofitIncidencias)
    }
}