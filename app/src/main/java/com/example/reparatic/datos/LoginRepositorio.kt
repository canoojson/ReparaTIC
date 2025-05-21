package com.example.reparatic.datos

import com.example.reparatic.conexion.Api
import com.example.reparatic.modelo.LoginRequest
import com.example.reparatic.modelo.Profesor

interface LoginRepositorio {
    suspend fun login(username: String, pwd: String): Profesor
}

class ConexionLoginRepositorio(private val api: Api) : LoginRepositorio {
    override suspend fun login(username: String, pwd: String): Profesor {
        val loginRequest = LoginRequest(username, pwd)
        return api.login(loginRequest)
    }
}