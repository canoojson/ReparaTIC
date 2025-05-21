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
import com.example.reparatic.datos.DepartamentoRepositorio
import com.example.reparatic.modelo.Departamento
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface DepartamentoUIState{
    data class ObtenerExito(val departamentos:List<Departamento>) : DepartamentoUIState
    data class CrearExito(val departamento: Departamento) : DepartamentoUIState
    data class ActualizarExito(val departamento: Response<Departamento>) : DepartamentoUIState
    data class EliminarExito(val id: Int) : DepartamentoUIState

    object Error: DepartamentoUIState
    object Cargando: DepartamentoUIState
}

class DepartamentoViewModel(private val departamentoRepositorio: DepartamentoRepositorio): ViewModel() {
    var departamentoUIState: DepartamentoUIState by mutableStateOf(DepartamentoUIState.Cargando)

    var departamentoPulsado: Departamento by mutableStateOf(Departamento(codDpto = 0, nombreDpto = ""))

    fun actualizarDepartamentoPulsado(departamento: Departamento){
        departamentoPulsado = departamento
    }

    init {
        obtenerDepartamentos()
    }

    fun obtenerDepartamentos() {
        viewModelScope.launch {
            departamentoUIState = DepartamentoUIState.Cargando
            departamentoUIState = try {
                val listaDepartamentos = departamentoRepositorio.obtenerDepartamentos()
                DepartamentoUIState.ObtenerExito(listaDepartamentos)
            } catch (e: IOException) {
                DepartamentoUIState.Error
            } catch (e: HttpException) {
                DepartamentoUIState.Error
            }
        }
    }

    fun insertarDepartamento(departamento: Departamento) {
        viewModelScope.launch {
            departamentoUIState = DepartamentoUIState.Cargando
            departamentoUIState = try {
                val departamentoInsertado =
                    departamentoRepositorio.insertarDepartamento(departamento)
                DepartamentoUIState.CrearExito(departamentoInsertado)
            } catch (e: IOException) {
                DepartamentoUIState.Error
            } catch (e: HttpException) {
                DepartamentoUIState.Error
            }
        }
    }

    fun actualizarDepartamento(id: Int, departamento: Departamento) {
        viewModelScope.launch {
            departamentoUIState = DepartamentoUIState.Cargando
            departamentoUIState = try {
                val departamentoActualizado =
                    departamentoRepositorio.actualizarDepartamento(id, departamento)
                DepartamentoUIState.ActualizarExito(departamentoActualizado)
            } catch (e: IOException) {
                DepartamentoUIState.Error
            } catch (e: HttpException) {
                DepartamentoUIState.Error
            }
        }
    }

    fun eliminarDepartamento(id: Int) {
        viewModelScope.launch {
            departamentoUIState = DepartamentoUIState.Cargando
            departamentoUIState = try {
                val departamentoEliminado = departamentoRepositorio.eliminarDepartamento(id)
                DepartamentoUIState.EliminarExito(id)
            } catch (e: IOException) {
                DepartamentoUIState.Error
            } catch (e: HttpException) {
                DepartamentoUIState.Error
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val departamentoRepositorio = aplicacion.contenedor.departamentoRepositorio
                DepartamentoViewModel(departamentoRepositorio = departamentoRepositorio)
            }
        }
    }
}