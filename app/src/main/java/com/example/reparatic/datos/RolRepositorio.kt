package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.Permiso
import com.example.reparatic.modelo.Rol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface RolRepositorio {
    suspend fun obtenerRoles(): List<Rol>
    suspend fun obtenerPermisos(): List<Permiso>
    suspend fun insertarRol(rol: Rol): Rol
    suspend fun actualizarRol(id: Int, rol: Rol): Response<Rol>
    suspend fun eliminarRol(id: Int): Response<Rol>
}

class conexionRolRepositorio(
    private val api: Api
) : RolRepositorio {
    override suspend fun obtenerRoles(): List<Rol> = api.obtenerRoles()
    override suspend fun obtenerPermisos(): List<Permiso> = api.obtenerPermisos()
    override suspend fun insertarRol(rol: Rol): Rol = api.insertarRol(rol)
    override suspend fun actualizarRol(id: Int, rol: Rol): Response<Rol>{
        val respuesta:Response<Rol>
        withContext(Dispatchers.IO) {
            respuesta = api.actualizarRol(id, rol)
        }
        return respuesta
    }
    override suspend fun eliminarRol(id: Int): Response<Rol> = api.eliminarRol(id)
}
