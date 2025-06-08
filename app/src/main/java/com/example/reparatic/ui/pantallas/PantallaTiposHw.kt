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
import com.example.reparatic.modelo.Permiso
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.TiposHw
import com.example.reparatic.ui.ViewModels.DepartamentoUIState
import com.example.reparatic.ui.ViewModels.TiposHwUIState
import com.example.reparatic.ui.ViewModels.UbicacionUIState

@Composable
fun PantallaInicioTiposHw(
    appUIState: TiposHwUIState,
    onTiposHwObtenidos: ()-> Unit,
    onTipoHwPulsado : (tipoHw: TiposHw) -> Unit,
    modifier: Modifier = Modifier
){
    when(appUIState){
        is TiposHwUIState.Error -> PantallaError(modifier= modifier.fillMaxWidth())
        is TiposHwUIState.Cargando -> PantallaCargando(modifier = modifier.fillMaxWidth())
        is TiposHwUIState.ObtenerExito -> PantallaTiposHw(
            lista = appUIState.tiposHw,
            modifier= modifier.fillMaxWidth(),
            onTipoHwPulsado = onTipoHwPulsado
        )
        is TiposHwUIState.ActualizarExito -> onTiposHwObtenidos()
        is TiposHwUIState.CrearExito -> onTiposHwObtenidos()
        is TiposHwUIState.EliminarExito -> onTiposHwObtenidos()
    }
}

@Composable
fun PantallaTiposHw(
    lista: List<TiposHw>,
    onTipoHwPulsado : (tipoHw: TiposHw) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn {
        items(lista) { tipoHw ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTipoHwPulsado(tipoHw)
                    }
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = tipoHw.descrip)
                }
            }
        }
    }
}

@Composable
fun TipoHwDialog(
    onDismiss: () -> Unit,
    onActualizarPulsado: (tipoHw: TiposHw) -> Unit,
    onEliminarPulsado: (tipoHw: TiposHw) -> Unit,
    onInsertarPulsado: (tipoHw: TiposHw) -> Unit,
    login:Profesor,
    tipoHw: TiposHw
) {
    var enModoEditar by remember {
        if(tipoHw.idTipoHw==0){
            mutableStateOf(true)
        }else{
            mutableStateOf(false)
        }
    }
    var nombreTipoHw by remember { mutableStateOf(tipoHw.descrip) }
    var tipoHwActualizado by remember { mutableStateOf(TiposHw(idTipoHw = tipoHw.idTipoHw, descrip = nombreTipoHw)) }
    val contexto = LocalContext.current
    val permiso = Permiso(codPermiso = 12, descrip = "Modificar/Eliminar departamentos")
    var error by remember { mutableStateOf(false) }

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
                    if(login.rol?.permisos?.contains(permiso) == true){
                        Button(
                            onClick = {
                                if(enModoEditar){
                                    if(tipoHw.idTipoHw==0){
                                        if(nombreTipoHw.isNotEmpty()){
                                            onInsertarPulsado(TiposHw(idTipoHw = 0, descrip = nombreTipoHw))
                                            Toast.makeText(contexto, "Tipo de hardware insertado correctamente", Toast.LENGTH_SHORT).show()
                                            enModoEditar = !enModoEditar
                                        }else{
                                            error = true
                                            Toast.makeText(contexto, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        if(nombreTipoHw.isNotEmpty()){
                                            tipoHwActualizado = TiposHw(idTipoHw = tipoHw.idTipoHw, descrip = nombreTipoHw)
                                            onActualizarPulsado(tipoHwActualizado)
                                            Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                                            enModoEditar = !enModoEditar
                                        }else{
                                            error = true
                                            Toast.makeText(contexto, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }else{
                                    enModoEditar = !enModoEditar
                                }

                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            if(enModoEditar || tipoHw.idTipoHw==0) {
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
                        if(tipoHw.idTipoHw!=0){
                            Button(onClick = {
                                onEliminarPulsado(tipoHw)
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
                }
                TextField(
                    value = nombreTipoHw,
                    onValueChange = { nombreTipoHw = it },
                    label = { Text("Nombre") },
                    isError = error,
                    modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp),
                    singleLine = true,
                    readOnly = !enModoEditar
                )
            }
        }
    }
}