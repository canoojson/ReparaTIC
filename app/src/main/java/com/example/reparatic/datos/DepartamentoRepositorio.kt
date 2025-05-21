package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.Departamento
import retrofit2.Response

interface DepartamentoRepositorio{
    suspend fun obtenerDepartamentos(): List<Departamento>
    suspend fun insertarDepartamento(departamento: Departamento): Departamento
    suspend fun actualizarDepartamento(id: Int, departamento: Departamento): Response<Departamento>
    suspend fun eliminarDepartamento(id: Int): Response<Departamento>
}

class conexionDepartamentoRepositorio(
    private val api: Api
): DepartamentoRepositorio{
    override suspend fun obtenerDepartamentos():List<Departamento> = api.obtenerDepartamentos()
    override suspend fun insertarDepartamento(departamento: Departamento): Departamento = api.insertarDepartamento(departamento)
    override suspend fun actualizarDepartamento(id: Int, departamento: Departamento): Response<Departamento> = api.actualizarDepartamento(id, departamento)
    override suspend fun eliminarDepartamento(id: Int): Response<Departamento> = api.eliminarDepartamento(id)
}