package com.example.reparatic.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import com.example.reparatic.ReparaTICAplicacion
import com.example.reparatic.datos.EstadoRepositorio
import com.example.reparatic.modelo.Estado
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface EstadoUIState {
    data class ObtenerExito(val estados: List<Estado>) : EstadoUIState
    data class CrearExito(val estado: Estado) : EstadoUIState
    data class ActualizarExito(val estado: Response<Estado>) : EstadoUIState
    data class EliminarExito(val id: Int) : EstadoUIState

    object Error : EstadoUIState
    object Cargando : EstadoUIState
}

class EstadoViewModel(private val estadoRepositorio: EstadoRepositorio) : ViewModel() {
    var estadoUIState: EstadoUIState by mutableStateOf(EstadoUIState.Cargando)

    var estadoPulsado: Estado by mutableStateOf(Estado(idEstado = 0, descrip = ""))

    fun actualizarEstadoPulsado(estado: Estado) {
        estadoPulsado = estado
    }

    init {
        obtenerEstados()
    }

    fun obtenerEstados() {
        viewModelScope.launch {
            estadoUIState = EstadoUIState.Cargando
            estadoUIState = try {
                val listaEstados = estadoRepositorio.obtenerEstados()
                EstadoUIState.ObtenerExito(listaEstados)
            } catch (e: IOException) {
                EstadoUIState.Error
            } catch (e: HttpException) {
                EstadoUIState.Error
            }
        }
    }

    fun insertarEstado(estado: Estado) {
        viewModelScope.launch {
            estadoUIState = EstadoUIState.Cargando
            estadoUIState = try {
                val estadoInsertado = estadoRepositorio.insertarEstado(estado)
                EstadoUIState.CrearExito(estadoInsertado)
            } catch (e: IOException) {
                EstadoUIState.Error
            } catch (e: HttpException) {
                EstadoUIState.Error
            }
        }
    }

    fun actualizarEstado(id: Int, estado: Estado) {
        viewModelScope.launch {
            estadoUIState = EstadoUIState.Cargando
            estadoUIState = try {
                val estadoActualizado = estadoRepositorio.actualizarEstado(id, estado)
                EstadoUIState.ActualizarExito(estadoActualizado)
            } catch (e: IOException) {
                EstadoUIState.Error
            } catch (e: HttpException) {
                EstadoUIState.Error
            }
        }
    }

    fun eliminarEstado(id: Int) {
        viewModelScope.launch {
            estadoUIState = EstadoUIState.Cargando
            estadoUIState = try {
                val estadoEliminado = estadoRepositorio.eliminarEstado(id)
                EstadoUIState.EliminarExito(id)
            } catch (e: IOException) {
                EstadoUIState.Error
            } catch (e: HttpException) {
                EstadoUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val estadoRepositorio = aplicacion.contenedor.estadoRepositorio
                EstadoViewModel(estadoRepositorio = estadoRepositorio)
            }
        }
    }
}


