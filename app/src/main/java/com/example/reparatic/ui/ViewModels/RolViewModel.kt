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
import com.example.reparatic.datos.RolRepositorio
import com.example.reparatic.modelo.Permiso
import com.example.reparatic.modelo.Rol
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface RolUIState {
    data class ObtenerExito(val roles: List<Rol>, val permisos: List<Permiso>) : RolUIState
    data class CrearExito(val rol: Rol) : RolUIState
    data class ActualizarExito(val rol: Response<Rol>) : RolUIState
    data class EliminarExito(val id: Int) : RolUIState

    object Error : RolUIState
    object Cargando : RolUIState
}

class RolViewModel(private val rolRepositorio: RolRepositorio) : ViewModel() {
    var rolUIState: RolUIState by mutableStateOf(RolUIState.Cargando)

    var rolPulsado: Rol by mutableStateOf(Rol(idRol = 0, descrip = "", permisos = emptyList()))

    fun actualizarRolPulsado(rol: Rol) {
        rolPulsado = rol
    }

    init {
        obtenerRoles()
        obtenerPermisos()
    }

    var listaRoles by mutableStateOf(listOf<Rol>())
    var listaPermisos by mutableStateOf(listOf<Permiso>())

    fun obtenerRoles() {
        viewModelScope.launch {
            rolUIState = RolUIState.Cargando
            rolUIState = try {
                listaRoles = rolRepositorio.obtenerRoles()
                RolUIState.ObtenerExito(listaRoles, listaPermisos)
            } catch (e: IOException) {
                RolUIState.Error
            } catch (e: HttpException) {
                RolUIState.Error
            }
        }
    }

    fun obtenerPermisos() {
        viewModelScope.launch {
            rolUIState = RolUIState.Cargando
            rolUIState = try {
                listaPermisos = rolRepositorio.obtenerPermisos()
                RolUIState.ObtenerExito(listaRoles, listaPermisos)
            } catch (e: IOException) {
                RolUIState.Error
            } catch (e: HttpException) {
                RolUIState.Error
            }
        }
    }

    fun insertarRol(rol: Rol) {
        viewModelScope.launch {
            rolUIState = RolUIState.Cargando
            rolUIState = try {
                val rolInsertado = rolRepositorio.insertarRol(rol)
                RolUIState.CrearExito(rolInsertado)
            } catch (e: IOException) {
                RolUIState.Error
            } catch (e: HttpException) {
                RolUIState.Error
            }
        }
    }

    suspend fun actualizarRol(id: Int, rol: Rol) {
        viewModelScope.launch {
            rolUIState = RolUIState.Cargando
            rolUIState = try {
                val rolActualizado = rolRepositorio.actualizarRol(id, rol)
                rolPulsado = rol
                RolUIState.ActualizarExito(rolActualizado)
            } catch (e: IOException) {
                RolUIState.Error
            } catch (e: HttpException) {
                RolUIState.Error
            }
        }
    }

    fun eliminarRol(id: Int) {
        viewModelScope.launch {
            rolUIState = RolUIState.Cargando
            rolUIState = try {
                val rolEliminado = rolRepositorio.eliminarRol(id)
                rolPulsado = Rol(idRol = 0, descrip = "", permisos = emptyList())
                RolUIState.EliminarExito(id)
            } catch (e: IOException) {
                RolUIState.Error
            } catch (e: HttpException) {
                RolUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val rolRepositorio = aplicacion.contenedor.rolRepositorio
                RolViewModel(rolRepositorio = rolRepositorio)
            }
        }
    }
}
