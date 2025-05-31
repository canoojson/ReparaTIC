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
import com.example.reparatic.datos.IncidenciaHardwareRepositorio
import com.example.reparatic.modelo.IncidenciaHardware
import kotlinx.coroutines.launch
import retrofit2.Response

sealed interface IncidenciaHardwareUIState {
    data class CargaExitosa(val incidencia: IncidenciaHardware) : IncidenciaHardwareUIState
    data class CreacionExitosa(val nueva: IncidenciaHardware) : IncidenciaHardwareUIState
    data class ActualizacionExitosa(val respuesta: Response<IncidenciaHardware>) : IncidenciaHardwareUIState
    data class EliminacionExitosa(val id: Int) : IncidenciaHardwareUIState

    object Cargando : IncidenciaHardwareUIState
    object Error : IncidenciaHardwareUIState
}

class IncidenciaHardwareViewModel(
    private val incidenciaRepositorio: IncidenciaHardwareRepositorio
) : ViewModel() {

    var estadoUI: IncidenciaHardwareUIState by mutableStateOf(IncidenciaHardwareUIState.Cargando)


    fun insertarIncidenciasHardware(incidencia: IncidenciaHardware) {
        viewModelScope.launch {
            estadoUI = IncidenciaHardwareUIState.Cargando
            estadoUI = try {
                val creada = incidenciaRepositorio.insertar(incidencia)
                IncidenciaHardwareUIState.CreacionExitosa(creada)
            } catch (e: Exception) {
                IncidenciaHardwareUIState.Error
            }
        }
    }

    fun actualizarIncidenciaHardware(id: Int, incidencia: IncidenciaHardware) {
        viewModelScope.launch {
            estadoUI = IncidenciaHardwareUIState.Cargando
            estadoUI = try {
                val respuesta = incidenciaRepositorio.actualizar(id, incidencia)
                IncidenciaHardwareUIState.ActualizacionExitosa(respuesta)
            } catch (e: Exception) {
                IncidenciaHardwareUIState.Error
            }
        }
    }

    fun eliminarIncidenciaHardware(id: Int) {
        viewModelScope.launch {
            estadoUI = IncidenciaHardwareUIState.Cargando
            estadoUI = try {
                val incidenciaHardware = incidenciaRepositorio.eliminar(id)
                IncidenciaHardwareUIState.EliminacionExitosa(id)
            } catch (e: Exception) {
                IncidenciaHardwareUIState.Error
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val incidenciaRepositorio = app.contenedor.incidenciaHardwareRepositorio
                IncidenciaHardwareViewModel(incidenciaRepositorio= incidenciaRepositorio)
            }
        }
    }
}
