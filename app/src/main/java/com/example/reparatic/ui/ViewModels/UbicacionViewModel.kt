package com.example.reparatic.ui.ViewModels

import android.util.Log
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
import com.example.reparatic.datos.UbicacionRepositorio
import com.example.reparatic.modelo.Ubicacion
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface UbicacionUIState {
    data class ObtenerExito(val ubicaciones: List<Ubicacion>) : UbicacionUIState
    data class CrearExito(val ubicacion: Ubicacion) : UbicacionUIState
    data class ActualizarExito(val ubicacion: Response<Ubicacion>, val ubicaciones: List<Ubicacion>) :
        UbicacionUIState
    data class EliminarExito(val id: Int) : UbicacionUIState

    object Error : UbicacionUIState
    object Cargando : UbicacionUIState
}

class UbicacionViewModel(private val ubicacionRepositorio: UbicacionRepositorio) : ViewModel() {
    var ubicacionUIState: UbicacionUIState by mutableStateOf(UbicacionUIState.Cargando)

    var ubicacionPulsada: Ubicacion by mutableStateOf(Ubicacion(idUbicacion = 0, nombre = "", descrip = ""))

    var listaUbicaciones: List<Ubicacion> by mutableStateOf(emptyList<Ubicacion>())

    fun actualizarUbicacionPulsada(ubicacion: Ubicacion) {
        ubicacionPulsada = ubicacion
    }

    init {
        obtenerUbicaciones()
    }

    fun obtenerUbicaciones() {
        viewModelScope.launch {
            ubicacionUIState = UbicacionUIState.Cargando
            ubicacionUIState = try {
                listaUbicaciones = ubicacionRepositorio.obtenerUbicaciones()
                UbicacionUIState.ObtenerExito(listaUbicaciones)
            } catch (e: IOException) {
                UbicacionUIState.Error
            } catch (e: HttpException) {
                UbicacionUIState.Error
            }
        }
    }

    fun insertarUbicacion(ubicacion: Ubicacion) {
        viewModelScope.launch {
            ubicacionUIState = UbicacionUIState.Cargando
            ubicacionUIState = try {
                val ubicacionInsertada = ubicacionRepositorio.insertarUbicacion(ubicacion)
                UbicacionUIState.CrearExito(ubicacionInsertada)
            } catch (e: IOException) {
                UbicacionUIState.Error
            } catch (e: HttpException) {
                UbicacionUIState.Error
            }
        }
    }

    fun actualizarUbicacion(id: Int, ubicacion: Ubicacion) {
        viewModelScope.launch {
            ubicacionUIState = UbicacionUIState.Cargando
            ubicacionUIState = try {
                val ubicacionActualizada = ubicacionRepositorio.actualizarUbicacion(id, ubicacion)
                obtenerUbicaciones()
                UbicacionUIState.ActualizarExito(ubicacionActualizada, listaUbicaciones)
            } catch (e: IOException) {
                UbicacionUIState.Error
            } catch (e: HttpException) {
                UbicacionUIState.Error
            }
        }
    }

    fun eliminarUbicacion(id: Int) {
        viewModelScope.launch {
            ubicacionUIState = UbicacionUIState.Cargando
            ubicacionUIState = try {
                val ubicacionEliminada = ubicacionRepositorio.eliminarUbicacion(id)
                Log.v("Ubicacion ELIMINADA", ubicacionEliminada.toString())
                UbicacionUIState.EliminarExito(id)
            } catch (e: IOException) {
                Log.v("Error", e.toString())
                UbicacionUIState.Error
            } catch (e: HttpException) {
                Log.v("Error", e.toString())
                UbicacionUIState.Error
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val ubicacionRepositorio = aplicacion.contenedor.ubicacionRepositorio
                UbicacionViewModel(ubicacionRepositorio = ubicacionRepositorio)
            }
        }
    }
}