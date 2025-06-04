package com.example.reparatic.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.ui.ViewModels.IncidenciaUIState

@Composable
fun PantallaInicioIncidencias(
    appUIState: IncidenciaUIState,
    login: Profesor,
    onIncidenciasObtenidas: ()-> Unit,
    onIncidenciaPulsada: (incidencia: Incidencia) -> Unit,
    modifier: Modifier = Modifier
){
    when(appUIState){
        is IncidenciaUIState.Error -> PantallaError(modifier= modifier.fillMaxWidth())
        is IncidenciaUIState.Cargando -> PantallaCargando(modifier = modifier.fillMaxWidth())
        is IncidenciaUIState.ObtenerExito -> PantallaIncidencias(
            lista = appUIState.incidencias,
            login = login,
            onIncidenciaPulsada = onIncidenciaPulsada,
            modifier= modifier.fillMaxWidth()
        )
        is IncidenciaUIState.ActualizarExito -> onIncidenciasObtenidas()
        is IncidenciaUIState.CrearExito -> onIncidenciasObtenidas()
        is IncidenciaUIState.EliminarExito -> onIncidenciasObtenidas()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaIncidencias(
    lista: List<Incidencia>,
    login: Profesor,
    onIncidenciaPulsada: (incidencia: Incidencia) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items(lista){ incidencia ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onIncidenciaPulsada(incidencia)
                    }
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row {
                            Text(text = incidencia.descripcion)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "#"+incidencia.idIncidencia.toString(),
                                color = Color.Gray,
                                fontSize = 14.sp)
                        }
                        Text(text = incidencia.profesor!!.nombre + " " + incidencia.profesor.apellidos)
                        Text(text= incidencia.fecha_incidencia.toString())
                    }
                    Column(horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier.fillMaxWidth()
                    )
                    {
                        Text(text = (incidencia.responsable?.nombre?: "Sin") + " " + (incidencia.responsable?.apellidos?:"Asignar"))
                        Text(text = incidencia.estado!!.descrip)
                        Text(text = incidencia.ubicacion!!.nombre)
                    }
                }
            }
        }
    }
}

