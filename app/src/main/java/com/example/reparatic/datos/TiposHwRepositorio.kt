package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.TiposHw
import retrofit2.Response

interface TiposHwRepositorio{
    suspend fun obtenerTiposHw(): List<TiposHw>
    suspend fun insertarTiposHw(tiposHw: TiposHw): TiposHw
    suspend fun actualizarTiposHw(id: Int, tiposHw: TiposHw): Response<TiposHw>
    suspend fun eliminarTiposHw(id: Int): Response<TiposHw>
}

class ConexionTiposHwRepositorio(
    private val api: Api
): TiposHwRepositorio{
    override suspend fun obtenerTiposHw(): List<TiposHw> = api.obtenerTiposHW()
    override suspend fun insertarTiposHw(tiposHw: TiposHw): TiposHw = api.insertarTipoHW(tiposHw)
    override suspend fun actualizarTiposHw(id: Int, tiposHw: TiposHw): Response<TiposHw> = api.actualizarTipoHW(id, tiposHw)
    override suspend fun eliminarTiposHw(id: Int): Response<TiposHw> = api.eliminarTipoHW(id)
}