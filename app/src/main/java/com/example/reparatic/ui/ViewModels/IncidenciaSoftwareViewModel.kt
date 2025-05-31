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
import com.example.reparatic.datos.IncidenciaSoftwareRepositorio
import com.example.reparatic.modelo.IncidenciaSoftware
import kotlinx.coroutines.launch
import retrofit2.Response

sealed interface IncidenciaSoftwareUIState {
    data class CargaExitosa(var incidenciaSoftware: IncidenciaSoftware) : IncidenciaSoftwareUIState
    data class CreacionExitosa(val nueva: IncidenciaSoftware) : IncidenciaSoftwareUIState
    data class ActualizacionExitosa(val respuesta: Response<IncidenciaSoftware>) : IncidenciaSoftwareUIState
    data class EliminacionExitosa(val id: Int) : IncidenciaSoftwareUIState

    object Cargando : IncidenciaSoftwareUIState
    object Error : IncidenciaSoftwareUIState
}
class IncidenciaSoftwareViewModel(
    private val repo: IncidenciaSoftwareRepositorio
) : ViewModel() {

    var estadoUI: IncidenciaSoftwareUIState by mutableStateOf(IncidenciaSoftwareUIState.Cargando)

    var incidenciaSoftware : IncidenciaSoftware? by mutableStateOf(null)


    fun insertar(incidencia: IncidenciaSoftware) {
        viewModelScope.launch {
            estadoUI = IncidenciaSoftwareUIState.Cargando
            estadoUI = try {
                val creada = repo.insertar(incidencia)
                IncidenciaSoftwareUIState.CreacionExitosa(creada)
            } catch (e: Exception) {
                IncidenciaSoftwareUIState.Error
            }
        }
    }

    fun actualizar(id: Int, incidencia: IncidenciaSoftware) {
        viewModelScope.launch {
            estadoUI = IncidenciaSoftwareUIState.Cargando
            estadoUI = try {
                val respuesta = repo.actualizar(id, incidencia)
                IncidenciaSoftwareUIState.ActualizacionExitosa(respuesta)
            } catch (e: Exception) {
                IncidenciaSoftwareUIState.Error
            }
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            estadoUI = IncidenciaSoftwareUIState.Cargando
            estadoUI = try {
                val incidenciaSoftware = repo.eliminar(id)
                IncidenciaSoftwareUIState.EliminacionExitosa(id)
            } catch (e: Exception) {
                IncidenciaSoftwareUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                IncidenciaSoftwareViewModel(app.contenedor.incidenciaSoftwareRepositorio)
            }
        }
    }
}
