package com.example.reparatic.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.reparatic.ReparaTICAplicacion
import com.example.reparatic.datos.LoginRepositorio
import com.example.reparatic.modelo.Profesor
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface LoginUIState {
    data class ObtenerExito(val profesor: Profesor) : LoginUIState
    data class Error(val mensaje: String) : LoginUIState

    object Cargando: LoginUIState
}

class LoginViewModel(
    private val loginRepositorio: LoginRepositorio
) : ViewModel() {

    var login : Profesor by mutableStateOf(Profesor(idProfesor = 0, nombre = "", apellidos = "", email = "", pwd = "" , departamento = null ,
        rol = null, dni = "", username = ""))

    var estado by mutableStateOf<LoginUIState>(LoginUIState.Cargando)
        private set

    fun iniciarSesion(username: String, pwd: String) {
        viewModelScope.launch {
            estado = LoginUIState.Cargando
            try {
                login = loginRepositorio.login(username, pwd)
                estado = LoginUIState.ObtenerExito(login)
            } catch (e: Exception) {
                estado = LoginUIState.Error("Login incorrecto")
            } catch (e: HttpException) {
                estado = LoginUIState.Error("Login incorrecto")
            }

        }
    }

    fun cerrarSesion(){
        estado = LoginUIState.Cargando
    }
    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val loginRepositorio = aplicacion.contenedor.loginRepositorio
                LoginViewModel(loginRepositorio = loginRepositorio)
            }
        }
    }
}