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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.reparatic.R
import com.example.reparatic.modelo.Ubicacion

@Composable
fun DialogoUbicacion(
    onDismiss: () -> Unit,
    ubicacion: Ubicacion,
    onUbicacionActualizada: (Ubicacion) -> Unit,
    onUbicacionEliminada:(id: Int) -> Unit){

    var nombre by remember { mutableStateOf(ubicacion.nombre) }
    var detalles by remember { mutableStateOf(ubicacion.descrip) }
    val contexto = LocalContext.current
    var ubicacionActualizada by remember { mutableStateOf(Ubicacion(idUbicacion = ubicacion.idUbicacion, nombre = nombre, descrip = detalles)) }
    Dialog(
        onDismissRequest = onDismiss
    ){
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row( ) {
                    Button(
                        onClick = {
                            ubicacionActualizada = Ubicacion(idUbicacion= ubicacion.idUbicacion, nombre = nombre, descrip = detalles)
                            onUbicacionActualizada(ubicacionActualizada)
                            Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Image(
                            modifier= Modifier.size(20.dp),
                            painter = painterResource(R.drawable.guardar_el_archivo),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Guardar"
                        )
                    }
                    /*
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
                        Text(text = "Eliminar")
                    }
                     */
                }
                Row {
                    TextField(
                        value = ubicacion.idUbicacion.toString(),
                        onValueChange = { },
                        label = { Text("Id") },
                        readOnly = true,
                        modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp)

                    )
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
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(text = "Cancelar")
                    }
                }
            }
        }
    }
}
@Composable
fun DialogoUbicacionDetalles(onDismiss: () -> Unit, ubicacion: Ubicacion){
    Dialog(
        onDismissRequest = onDismiss
    ){
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = ubicacion.nombre,
                    onValueChange = { },
                    label = { Text("Nombre") },
                    modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp),
                    readOnly = true
                )
                TextField(
                    value = ubicacion.descrip,
                    onValueChange = { },
                    label = { Text("Más detalles") },
                    readOnly = true,
                    modifier = Modifier.height(100.dp)
                        .width(400.dp)
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.padding(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(text = "Cerrar")
                    }
                }

            }
        }
    }
}