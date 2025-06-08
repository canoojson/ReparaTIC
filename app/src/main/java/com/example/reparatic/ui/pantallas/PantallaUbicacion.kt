package com.example.reparatic.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.reparatic.R
import com.example.reparatic.modelo.Permiso
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.Ubicacion

@Composable
fun PantallaUbicacion(
    ubicacion: Ubicacion,
    login : Profesor,
    onUbicacionInsertada : (Ubicacion) -> Unit,
    onUbicacionActualizada: (Ubicacion) -> Unit,
    onUbicacionEliminada:(id: Int) -> Unit
){

    val permiso = Permiso(codPermiso = 11, descrip = "Modificar/Eliminar ubicaciones")
    var idUbicacion by remember { mutableStateOf(ubicacion.idUbicacion) }
    var nombre by remember { mutableStateOf(ubicacion.nombre) }
    var detalles by remember { mutableStateOf(ubicacion.descrip) }
    val contexto = LocalContext.current
    var ubicacionActualizada by remember { mutableStateOf(Ubicacion(idUbicacion = ubicacion.idUbicacion, nombre = nombre, descrip = detalles)) }
    var isEditable by remember {
        if(idUbicacion==0){
            mutableStateOf(true)
        }else{
            mutableStateOf(false)
        }}
    var enModoEdicion by remember { if(idUbicacion==0){
        mutableStateOf(true)
    }else{
        mutableStateOf(false)
    } }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row( ) {
                    if(login.rol?.permisos?.contains(permiso) == true){
                        Button(
                            onClick = {
                                if(enModoEdicion){
                                    if(ubicacion.idUbicacion == 0){
                                        onUbicacionInsertada(Ubicacion(idUbicacion=0, nombre = nombre, descrip = detalles))
                                        Toast.makeText(
                                            contexto,
                                            "Ubicación insertada correctamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }else{
                                        ubicacionActualizada = Ubicacion(
                                            idUbicacion = ubicacion.idUbicacion,
                                            nombre = nombre,
                                            descrip = detalles
                                        )
                                        onUbicacionActualizada(ubicacionActualizada)
                                        Toast.makeText(
                                            contexto,
                                            "Cambios guardados correctamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }else{
                                    isEditable = true
                                    enModoEdicion = true
                                }
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            if(enModoEdicion){
                                Image(
                                    modifier= Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.guardar_el_archivo),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Guardar"
                                )
                            }else{
                                Image(
                                    modifier= Modifier.size(20.dp),
                                    imageVector = Icons.Filled.Create,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Editar"
                                )
                            }

                        }

                        Button(onClick = {
                            onUbicacionEliminada(ubicacion.idUbicacion)
                            ubicacionActualizada = Ubicacion(idUbicacion= 0, nombre = "", descrip = "")
                            Toast.makeText(contexto, "Ubicación eliminada correctamente", Toast.LENGTH_SHORT).show()
                        },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(Color.Red, Color.Black, Color.Red, Color.Black),
                            modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                        ) {
                            Text(text = stringResource(R.string.eliminar))
                        }
                    }
                }
                Row {
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp)
                    )
                }
                TextField(
                    value = detalles,
                    onValueChange = { detalles = it },
                    label = { Text("Más detalles") },
                    modifier = Modifier.height(100.dp)
                        .width(400.dp)
                )
            }
        }
    }
}