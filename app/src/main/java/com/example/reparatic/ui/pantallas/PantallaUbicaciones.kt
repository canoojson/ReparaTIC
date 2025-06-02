package com.example.reparatic.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.reparatic.modelo.Ubicacion
import com.example.reparatic.ui.ViewModels.IncidenciaUIState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.reparatic.ui.ViewModels.UbicacionUIState

@Composable
fun PantallaInicioUbicaciones(
    appUIState: UbicacionUIState,
    onUbicacionesObtenidas: ()-> Unit,
    onUbicacionPulsada: (ubicacion: Ubicacion) -> Unit,
    modifier: Modifier = Modifier
){
    when(appUIState){
        is UbicacionUIState.Error -> PantallaError(modifier= modifier.fillMaxWidth())
        is UbicacionUIState.Cargando -> PantallaCargando(modifier = modifier.fillMaxWidth())
        is UbicacionUIState.ObtenerExito -> PantallaUbicaciones(
            lista = appUIState.ubicaciones,
            onUbicacionPulsada = onUbicacionPulsada,
            modifier= modifier.fillMaxWidth()
        )
        is UbicacionUIState.ActualizarExito -> onUbicacionesObtenidas()
        is UbicacionUIState.CrearExito -> onUbicacionesObtenidas()
        is UbicacionUIState.EliminarExito -> onUbicacionesObtenidas()
    }
}

@Composable
fun PantallaUbicaciones(
    lista: List<Ubicacion>,
    onUbicacionPulsada: (ubicacion: Ubicacion) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier){
        items(lista){ ubicacion ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onUbicacionPulsada(ubicacion)
                    }
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = ubicacion.nombre)
                }
            }
        }
    }
}