package com.example.reparatic.ui

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
import com.example.reparatic.datos.IncidenciaRepositorio
import com.example.reparatic.modelo.Incidencia
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface IncidenciaUIState {
    data class ObtenerExito(val incidencias:List<Incidencia>) : IncidenciaUIState
    data class CrearExito(val incidencia: Incidencia) : IncidenciaUIState
    data class ActualizarExito(val incidencia: Response<Incidencia>) : IncidenciaUIState
    data class EliminarExito(val id: Int) : IncidenciaUIState

    object Error: IncidenciaUIState
    object Cargando: IncidenciaUIState
}

class IncidenciaViewModel(private val incidenciaRepositorio: IncidenciaRepositorio): ViewModel() {
    var incidenciaUIState: IncidenciaUIState by mutableStateOf(IncidenciaUIState.Cargando)

    var incidenciaPulsada: Incidencia by mutableStateOf(Incidencia(idIncidencia = 0,tipo="", fecha_incidencia = null,
        fecha_introduccion = "", profesor = null, departamento = null, ubicacion = null, descripcion = "",
        observaciones = "", estado = null, responsable = null, fecha_resolucion = "", tiempo_invertido = "",
        mas_info = ByteArray(0), comentarios = emptyList(), incidenciaHardware = null, incidenciaSoftware = null))
        private set
    fun actualizarIncidenciaPulsada(incidencia: Incidencia){
        incidenciaPulsada = incidencia
    }

    init {
        obtenerIncidencias()
    }

    fun obtenerIncidencias() {
        viewModelScope.launch {
            incidenciaUIState = IncidenciaUIState.Cargando
            incidenciaUIState = try {
                val listaIncidencias = incidenciaRepositorio.obtenerIncidencias()
                IncidenciaUIState.ObtenerExito(listaIncidencias)
            } catch (e: IOException) {
                IncidenciaUIState.Error
            } catch (e: HttpException) {
                IncidenciaUIState.Error
            }
        }
    }

    fun insertarIncidencia(incidencia: Incidencia) {
        viewModelScope.launch {
            incidenciaUIState = IncidenciaUIState.Cargando
            incidenciaUIState = try {
                val incidenciaInsertada = incidenciaRepositorio.insertarIncidencia(incidencia)
                IncidenciaUIState.CrearExito(incidenciaInsertada)
            } catch (e: IOException) {
                IncidenciaUIState.Error
            } catch (e: HttpException) {
                IncidenciaUIState.Error
            }
        }
    }

    fun actualizarIncidencia(id: Int, incidencia: Incidencia) {
        viewModelScope.launch {
            incidenciaUIState = IncidenciaUIState.Cargando
            incidenciaUIState = try {
                val incidenciaActualizada =
                    incidenciaRepositorio.actualizarIncidencia(id, incidencia)
                IncidenciaUIState.ActualizarExito(incidenciaActualizada)
            } catch (e: IOException) {
                IncidenciaUIState.Error
            } catch (e: HttpException) {
                IncidenciaUIState.Error
            }
        }
    }

    fun eliminarIncidencia(id: Int) {
        viewModelScope.launch {
            incidenciaUIState = IncidenciaUIState.Cargando
            incidenciaUIState = try {
                val incidenciaEliminada = incidenciaRepositorio.eliminarIncidencia(id)
                IncidenciaUIState.EliminarExito(id)
            } catch (e: IOException) {
                IncidenciaUIState.Error
            } catch (e: HttpException) {
                IncidenciaUIState.Error
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val incidenciaRepositorio = aplicacion.contenedor.incidenciaRepositorio
                IncidenciaViewModel(incidenciaRepositorio = incidenciaRepositorio)
            }
        }
    }
}