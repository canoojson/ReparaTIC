package com.example.reparatic.ui.pantallas

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.ui.ViewModels.ProfesorUIState
import com.example.reparatic.ui.ViewModels.UbicacionUIState

@Composable
fun PantallaInicioProfesores(
    appUIState: ProfesorUIState,
    onProfesoresObtenidos: ()-> Unit,
    onProfesorPulsado : (profesor: Profesor) -> Unit,
    modifier: Modifier = Modifier
){
    when(appUIState){
        is ProfesorUIState.Error -> PantallaError(modifier= modifier.fillMaxWidth())
        is ProfesorUIState.Cargando -> PantallaCargando(modifier = modifier.fillMaxWidth())
        is ProfesorUIState.ObtenerExito -> PantallaProfesores(
            lista = appUIState.profesores,
            onProfesorPulsado = onProfesorPulsado,
            modifier= modifier.fillMaxWidth()
        )
        is ProfesorUIState.ActualizarExito -> onProfesoresObtenidos()
        is ProfesorUIState.CrearExito -> onProfesoresObtenidos()
        is ProfesorUIState.EliminarExito -> onProfesoresObtenidos()
    }
}

@Composable
fun PantallaProfesores(
    lista: List<Profesor>,
    onProfesorPulsado: (profesor: Profesor) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier){
        items(lista){ profesor ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onProfesorPulsado(profesor)
                    }
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Image(
                        imageVector = Icons.Filled.Face,
                        contentDescription = "Foto de perfil"
                    )
                    Text(text = profesor.nombre + " " + profesor.apellidos)
                }
            }
        }
    }
}