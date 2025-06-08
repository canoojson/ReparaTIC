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
import com.example.reparatic.datos.ProfesorRepositorio
import com.example.reparatic.modelo.Profesor
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface ProfesorUIState {
    data class ObtenerExito(val profesores:List<Profesor>, val profesoresInformatica: List<Profesor>) : ProfesorUIState
    data class CrearExito(val profesor: Profesor) : ProfesorUIState
    data class ActualizarExito(val profesor: Response<Profesor>) : ProfesorUIState
    data class EliminarExito(val id: Int) : ProfesorUIState

    object Error: ProfesorUIState
    object Cargando: ProfesorUIState
}

class ProfesorViewModel(private val profesorRepositorio: ProfesorRepositorio): ViewModel(){
    var profesorUIState: ProfesorUIState by mutableStateOf(ProfesorUIState.Cargando)

    var profesorPulsado: Profesor by mutableStateOf(Profesor(nombre = "", departamento = null,
        email = "", pwd = "", rol = null, username = "", apellidos = "", dni = "", idProfesor = 0))
        private set
    fun actualizarProfesorPulsado(profesor: Profesor){
        profesorPulsado = profesor
    }

    var listaProfesores: List<Profesor> by mutableStateOf(listOf())
    var listaProfesoresDepto: List<Profesor> by mutableStateOf(listOf())

    init {
        obtenerProfesores()
    }

    fun obtenerProfesores() {
        viewModelScope.launch {
            profesorUIState = ProfesorUIState.Cargando
            profesorUIState = try {
                 listaProfesores = profesorRepositorio.obtenerProfesores()
                ProfesorUIState.ObtenerExito(listaProfesores, listaProfesoresDepto)
            } catch (e: IOException) {
                ProfesorUIState.Error
            } catch (e: HttpException) {
                ProfesorUIState.Error
            }
        }
    }

    fun obtenerProfesoresDepartamento(nombre: String) {
        viewModelScope.launch {
            profesorUIState = ProfesorUIState.Cargando
            profesorUIState = try {
                listaProfesoresDepto = profesorRepositorio.obtenerProfesoresDepartamento(nombre)
                ProfesorUIState.ObtenerExito(listaProfesores, listaProfesoresDepto)
            }catch (e: IOException) {
                ProfesorUIState.Error
            } catch (e: HttpException) {
                ProfesorUIState.Error
            }
        }
    }

    fun insertarProfesor(profesor: Profesor) {
        viewModelScope.launch {
            profesorUIState = ProfesorUIState.Cargando
            profesorUIState = try {
                val profesorInsertado = profesorRepositorio.insertarProfesor(profesor)
                ProfesorUIState.CrearExito(profesorInsertado)
            } catch (e: IOException) {
                ProfesorUIState.Error
            } catch (e: HttpException) {
                ProfesorUIState.Error
            }
        }
    }

    fun actualizarProfesor(id: Int, profesor: Profesor) {
        viewModelScope.launch {
            profesorUIState = ProfesorUIState.Cargando
            profesorUIState = try {
                val profesorActualizado =
                    profesorRepositorio.actualizarProfesor(id, profesor)
                ProfesorUIState.ActualizarExito(profesorActualizado)
            }catch (e: IOException) {
                ProfesorUIState.Error
            } catch (e: HttpException) {
                ProfesorUIState.Error
            }
        }
    }

    fun eliminarProfesor(id: Int) {
        viewModelScope.launch {
            profesorUIState = ProfesorUIState.Cargando
            profesorUIState = try {
                val profesorEliminado = profesorRepositorio.eliminarProfesor(id)
                ProfesorUIState.EliminarExito(id)
            } catch (e: IOException) {
                ProfesorUIState.Error
            } catch (e: HttpException) {
                ProfesorUIState.Error
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = (this[APPLICATION_KEY] as ReparaTICAplicacion)
                val profesorRepositorio = aplicacion.contenedor.profesorRepositorio
                ProfesorViewModel(profesorRepositorio = profesorRepositorio)
            }
        }
    }
}