package com.example.reparatic.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.reparatic.R
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.reparatic.modelo.Departamento
import com.example.reparatic.ui.ViewModels.DepartamentoUIState
import com.example.reparatic.ui.ViewModels.UbicacionUIState

@Composable
fun PantallaInicioDepartamentos(
    appUIState: DepartamentoUIState,
    onDepartamentosObtenidos: ()-> Unit,
    onDepartamentoPulsado : (departamento: Departamento) -> Unit,
    modifier: Modifier = Modifier
){
    when(appUIState){
        is DepartamentoUIState.Error -> PantallaError(modifier= modifier.fillMaxWidth())
        is DepartamentoUIState.Cargando -> PantallaCargando(modifier = modifier.fillMaxWidth())
        is DepartamentoUIState.ObtenerExito -> PantallaDepartamentos(
            lista = appUIState.departamentos,
            modifier= modifier.fillMaxWidth(),
            onDepartamentoPulsado = onDepartamentoPulsado
        )
        is DepartamentoUIState.ActualizarExito -> onDepartamentosObtenidos()
        is DepartamentoUIState.CrearExito -> onDepartamentosObtenidos()
        is DepartamentoUIState.EliminarExito -> onDepartamentosObtenidos()
    }
}

@Composable
fun PantallaDepartamentos(
    lista: List<Departamento>,
    onDepartamentoPulsado : (departamento: Departamento) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn {
        items(lista) { departamento ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDepartamentoPulsado(departamento)
                    }
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = departamento.nombreDpto)
                }
            }
        }
    }
}

@Composable
fun DepartamentoDialog(
    onDismiss: () -> Unit,
    onActualizarPulsado: (departamento: Departamento) -> Unit,
    onEliminarPulsado: (departamento: Departamento) -> Unit,
    onInsertarPulsado: (departamento: Departamento) -> Unit,
    departamento: Departamento
) {
    var enModoEditar by remember {
        if(departamento.codDpto==0){
            mutableStateOf(true)
        }else{
            mutableStateOf(false)
        }
    }
    var nombreDepartamento by remember { mutableStateOf(departamento.nombreDpto) }
    var departamentoActualizado by remember { mutableStateOf(Departamento(codDpto = departamento.codDpto, nombreDpto = nombreDepartamento)) }
    val contexto = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss
    ){
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row( ) {
                    Button(
                        onClick = {
                            if(departamento.codDpto==0){
                                onInsertarPulsado(Departamento(codDpto = 0,nombreDpto = nombreDepartamento))
                                Toast.makeText(contexto, "Departamento insertado correctamente", Toast.LENGTH_SHORT).show()
                            }else if(enModoEditar){
                                departamentoActualizado = Departamento(codDpto = departamento.codDpto, nombreDpto = nombreDepartamento)
                                onActualizarPulsado(departamentoActualizado)
                                Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                            }
                            enModoEditar = !enModoEditar
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        if(enModoEditar || departamento.codDpto==0) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(R.drawable.guardar_el_archivo),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Guardar"
                            )
                        }else{
                            Image(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Filled.Create,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Editar")
                        }
                    }
                    if(departamento.codDpto!=0){
                        Button(onClick = {
                            onEliminarPulsado(departamento)
                        },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            ),
                            colors = ButtonColors(Color.Red, Color.Black, Color.Red, Color.Black),
                            modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(20.dp),
                                imageVector = Icons.Filled.Delete,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Eliminar"
                            )
                        }
                    }
                }
                TextField(
                    value = nombreDepartamento,
                    onValueChange = { nombreDepartamento = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp),
                    singleLine = true,
                    readOnly = !enModoEditar
                )
            }
        }
    }
}