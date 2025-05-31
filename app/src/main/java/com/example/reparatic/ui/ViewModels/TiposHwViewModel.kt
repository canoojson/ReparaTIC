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
import com.example.reparatic.datos.TiposHwRepositorio
import com.example.reparatic.modelo.TiposHw
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface TiposHwUIState {
    data class ObtenerExito(val tiposHw:List<TiposHw>) : TiposHwUIState
    data class CrearExito(val tiposHw: TiposHw) : TiposHwUIState
    data class ActualizarExito(val tiposHw: Response<TiposHw>) : TiposHwUIState
    data class EliminarExito(val id: Int) : TiposHwUIState

    object Error: TiposHwUIState
    object Cargando: TiposHwUIState
}

class TiposHwViewModel(private val tiposHwRepositorio: TiposHwRepositorio): ViewModel(){
    var tiposHwUIState: TiposHwUIState by mutableStateOf(TiposHwUIState.Cargando)

    var tiposHwPulsado: TiposHw by mutableStateOf(TiposHw(idTipoHw = 0, descrip = ""))
        private set
    fun actualizarTiposHwPulsado(tiposHw: TiposHw){
        tiposHwPulsado = tiposHw
    }

    init {
        obtenerTiposHw()
    }

    fun obtenerTiposHw() {
        viewModelScope.launch {
            tiposHwUIState = TiposHwUIState.Cargando
            tiposHwUIState = try {
                val listaTiposHw = tiposHwRepositorio.obtenerTiposHw()
                TiposHwUIState.ObtenerExito(listaTiposHw)
            } catch (e: IOException) {
                TiposHwUIState.Error
            } catch (e: HttpException) {
                TiposHwUIState.Error
            }
        }
    }

    fun insertarTiposHw(tiposHw: TiposHw) {
        viewModelScope.launch {
            tiposHwUIState = TiposHwUIState.Cargando
            tiposHwUIState = try {
                val tiposHwInsertada = tiposHwRepositorio.insertarTiposHw(tiposHw)
                TiposHwUIState.CrearExito(tiposHwInsertada)
            }catch (e: IOException) {
                TiposHwUIState.Error
            } catch (e: HttpException) {
                TiposHwUIState.Error
            }
        }
    }

    fun actualizarTiposHw(id: Int, tiposHw: TiposHw) {
        viewModelScope.launch {
            tiposHwUIState = TiposHwUIState.Cargando
            tiposHwUIState = try {
                val tiposHw = tiposHwRepositorio.actualizarTiposHw(id, tiposHw)
                TiposHwUIState.ActualizarExito(tiposHw)
            } catch (e: IOException) {
                TiposHwUIState.Error
            } catch (e: HttpException) {
                TiposHwUIState.Error
            }
        }
    }

    fun eliminarTiposHw(id: Int) {
        viewModelScope.launch {
            tiposHwUIState = TiposHwUIState.Cargando
            tiposHwUIState = try {
                val tiposHw = tiposHwRepositorio.eliminarTiposHw(id)
                TiposHwUIState.EliminarExito(id)
            } catch (e: IOException) {
                TiposHwUIState.Error
            } catch (e: HttpException) {
                TiposHwUIState.Error
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val tiposHwRepositorio = aplicacion.contenedor.tiposHwRepositorio
                TiposHwViewModel(tiposHwRepositorio = tiposHwRepositorio)
            }
        }
    }
}