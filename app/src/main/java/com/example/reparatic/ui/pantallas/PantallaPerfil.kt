package com.example.reparatic.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.sp
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor

@Composable
fun PantallaPerfil(
    profesor: Profesor
){
    //Variables
    var modoEdicion by remember { mutableStateOf(false) }
    val contexto = LocalContext.current

    var nombre by remember { mutableStateOf(profesor.nombre) }
    var apellidos by remember { mutableStateOf(profesor.apellidos) }
    var email by remember { mutableStateOf(profesor.email) }
    var password by remember { mutableStateOf(profesor.pwd) }
    var departamento by remember { mutableStateOf(profesor.departamento) }
    var rol by remember { mutableStateOf(profesor.rol) }
    var username by remember { mutableStateOf(profesor.username) }
    var dni by remember { mutableStateOf(profesor.dni) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier
                .width(400.dp)
                .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                        .padding(0.dp,0.dp,0.dp,150.dp)) {
                    Image(
                        modifier= Modifier.size(250.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                    Text(profesor.nombre,
                        fontSize = 50.sp )
                    Text(profesor.apellidos,
                        fontSize = 50.sp)

                }
            }
            Column {
                Row {
                    if(modoEdicion){
                        Button(
                            onClick = {
                                //val profesorAct = Profesor()

                                Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                                modoEdicion = false
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            ),
                            modifier = Modifier.padding(32.dp, 32.dp,0.dp,0.dp)
                        ) {
                            Text(text = "Guardar")
                        }
                        //Si no lo estamos, mostramos el de editar

                    }else{
                        Button(
                            onClick = {
                                modoEdicion = true
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            ),
                            modifier = Modifier.padding(32.dp, 32.dp,0.dp,0.dp)
                        ) {
                            Text(text = "Editar")
                        }
                    }
                    Button(onClick = {

                    },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        ),
                        colors = ButtonColors(Color.Red, Color.Black, Color.Red, Color.Black),
                        modifier = Modifier.padding(16.dp, 32.dp,0.dp,0.dp)
                    ) {
                        Text(text = "Eliminar")
                    }
                }
                Row {
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(180.dp)
                            .padding(32.dp,32.dp,0.dp,0.dp)
                    )
                    TextField(
                        value = apellidos,
                        onValueChange = { apellidos = it },
                        label = { Text("Apellidos") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(180.dp)
                            .padding(16.dp,32.dp,0.dp,0.dp)
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(400.dp)
                            .padding(16.dp,32.dp,0.dp,0.dp)
                    )
                }
            }
        }
    }

}