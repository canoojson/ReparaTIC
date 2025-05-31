package com.example.reparatic.ui.pantallas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.reparatic.R
import com.example.reparatic.modelo.Departamento
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.Rol
import com.example.reparatic.ui.ViewModels.DepartamentoUIState
import com.example.reparatic.ui.ViewModels.ProfesorUIState
import com.example.reparatic.ui.ViewModels.RolUIState
import com.example.reparatic.ui.encriptarMD5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    profesor: Profesor,
    uiStateDepto: DepartamentoUIState,
    uiStateRol: RolUIState,
    uiStateProfesor : ProfesorUIState,
    onActualizarPulsado : (Profesor) -> Unit,
    onEliminarPulsado : (Profesor) -> Unit
) {
    //Variables
    var modoEdicion by remember { mutableStateOf(false) }
    var mostrarDialogContraseña by remember { mutableStateOf(false) }
    val contexto = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }

        //Variables Perfil
    var perfil by remember { mutableStateOf(profesor) }
    var nombre by remember { mutableStateOf(perfil.nombre) }
    var apellidos by remember { mutableStateOf(perfil.apellidos) }
    var email by remember { mutableStateOf(perfil.email) }
    var pwd by remember { mutableStateOf(perfil.pwd) }
    var departamento by remember { mutableStateOf(perfil.departamento) }
    var rol by remember { mutableStateOf(perfil.rol) }
    var username by remember { mutableStateOf(perfil.username) }
    var dni by remember { mutableStateOf(perfil.dni) }

    var listaDepartamentos = emptyList<Departamento>()
    var listaRoles = emptyList<Rol>()

    when (uiStateProfesor) {
        is ProfesorUIState.ActualizarExito -> {
            Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
            perfil = uiStateProfesor.profesor.body()!!
        }
        is ProfesorUIState.Error -> {
            PantallaError()
            Toast.makeText(contexto, "Ha ocurrido un problema al guardar los cambios." +
                    "Inténtalo mas tarde y si el error persiste, contacta al administrador", Toast.LENGTH_LONG).show()
        }

        is ProfesorUIState.EliminarExito -> {
            Toast.makeText(contexto, "Perfil eliminado correctamente", Toast.LENGTH_SHORT).show()
        }

        else -> {

        }
    }

    when (uiStateDepto) {
        is DepartamentoUIState.ObtenerExito ->
            listaDepartamentos = uiStateDepto.departamentos

        else -> PantallaError(modifier = Modifier.fillMaxWidth())
    }
    when (uiStateRol) {
        is RolUIState.ObtenerExito ->
            listaRoles = uiStateRol.roles

        else -> PantallaError(modifier = Modifier.fillMaxWidth())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .width(400.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 150.dp)
                ) {
                    Image(
                        modifier = Modifier.size(250.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Foto de perfil"
                    )
                    Text(
                        perfil.nombre,
                        fontSize = 50.sp
                    )
                    Text(
                        perfil.apellidos,
                        fontSize = 50.sp
                    )

                }
            }
            Column {
                Row {
                    if (modoEdicion) {
                        Button(
                            onClick = {
                                modoEdicion = false
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            ),
                            modifier = Modifier.padding(32.dp, 32.dp, 0.dp, 0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(20.dp),
                                painter = painterResource(R.drawable.guardar_el_archivo),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                        //Si no lo estamos, mostramos el de editar

                    } else {
                        Button(
                            onClick = {
                                modoEdicion = true
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            ),
                            modifier = Modifier.padding(32.dp, 32.dp, 0.dp, 0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(20.dp),
                                imageVector = Icons.Filled.Edit,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }
                    Button(
                        onClick = {

                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        ),
                        colors = ButtonColors(Color.Red, Color.Black, Color.Red, Color.Black),
                        modifier = Modifier.padding(16.dp, 32.dp, 0.dp, 0.dp)
                    ) {
                        Image(
                            modifier= Modifier.size(20.dp),
                            imageVector = Icons.Filled.Delete,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Eliminar"
                        )
                    }
                    Button(
                        onClick = {
                            mostrarDialogContraseña = true
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        ),
                        modifier = Modifier.padding(16.dp, 32.dp, 0.dp, 0.dp)
                    ) {
                        Text(text = "Cambiar contraseña")
                    }
                }
                Row {
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(180.dp)
                            .padding(32.dp, 32.dp, 0.dp, 0.dp)
                    )
                    TextField(
                        value = apellidos,
                        onValueChange = { apellidos = it },
                        label = { Text("Apellidos") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(180.dp)
                            .padding(16.dp, 32.dp, 0.dp, 0.dp)
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(400.dp)
                            .padding(16.dp, 32.dp, 0.dp, 0.dp)
                    )
                }
                Row {
                    TextField(
                        value = dni,
                        onValueChange = { dni = it },
                        label = { Text("DNI") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(180.dp)
                            .padding(32.dp, 16.dp, 0.dp, 0.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            if (modoEdicion) {
                                expanded = !expanded
                            }
                        },
                        modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp)
                    ) {
                        TextField(
                            value = departamento!!.nombreDpto,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Departamento") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            listaDepartamentos.forEach { departamentos ->
                                DropdownMenuItem(
                                    text = { Text(text = departamentos.nombreDpto) },
                                    onClick = {
                                        departamento = departamentos
                                        expanded = false
                                    },
                                    enabled = modoEdicion
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = expanded2,
                        onExpandedChange = {
                            if (modoEdicion) {
                                expanded2 = !expanded2
                            }
                        },
                        modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp)
                    ) {
                        TextField(
                            value = rol!!.descrip,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Rol") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                        ) {
                            listaRoles.forEach { roles ->
                                DropdownMenuItem(
                                    text = { Text(text = roles.descrip) },
                                    onClick = {
                                        rol = roles
                                        expanded2 = false
                                    },
                                    enabled = modoEdicion
                                )
                            }
                        }
                    }
                }
                Row(modifier = Modifier.padding(32.dp, 16.dp, 0.dp, 0.dp)) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre de usuario") },
                        readOnly = !modoEdicion,
                        modifier = Modifier.width(200.dp)
                            .padding(0.dp,0.dp,16.dp,0.dp)
                    )
                }
            }
        }
    }
    if (mostrarDialogContraseña) {
        DialogCambioContraseña(profesor = profesor, onPwdChange = { nueva -> pwd = nueva }, onDismiss = { mostrarDialogContraseña = false }, onActualizarProfesor = onActualizarPulsado,  contexto = contexto)
    }
}

@Composable
fun DialogCambioContraseña(
    profesor: Profesor,
    onDismiss: () -> Unit,
    onPwdChange: (String) -> Unit,
    onActualizarProfesor: (Profesor) -> Unit,
    contexto: Context,
    modifier: Modifier = Modifier
){

    var verContraseña by remember { mutableStateOf(false) }
    var verContraseña2 by remember { mutableStateOf(false) }
    var verContraseña3 by remember { mutableStateOf(false) }
    var pwd by remember { mutableStateOf(profesor.pwd) }
    var pwdActual by remember { mutableStateOf("") }
    var pwdActualMD5 by remember { mutableStateOf("") }
    var nuevaPwd by remember { mutableStateOf("") }
    var nuevaPwd2 by remember { mutableStateOf("") }

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
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               Text("Por motivos de seguridad, debes introducir tu contraseña actual y repetir dos veces la nueva contraseña")
                //CONTRASEÑA ACTUAL
                Row(modifier = Modifier.padding(16.dp)) {
                    if(!verContraseña) {
                        TextField(
                            value = pwdActual,
                            onValueChange = { pwdActual = it },
                            label = {
                                Text("Contraseña")
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.width(200.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                verContraseña = !verContraseña
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(15.dp),
                                painter = painterResource(R.drawable.ojo),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }else{
                        TextField(
                            value = pwdActual,
                            onValueChange = { pwdActual = it },
                            label = {
                                Text("Contraseña")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.width(200.dp),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                verContraseña = !verContraseña
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(15.dp),
                                painter = painterResource(R.drawable.invisible),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }
                }
                //NUEVA CONTRASEÑA
                Row(modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp)) {
                    if(!verContraseña) {
                        TextField(
                            value = nuevaPwd,
                            onValueChange = { nuevaPwd = it },
                            label = {
                                Text("Contraseña nueva")
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.width(200.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                verContraseña2 = !verContraseña2
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(15.dp),
                                painter = painterResource(R.drawable.ojo),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }else{
                        TextField(
                            value = nuevaPwd,
                            onValueChange = { nuevaPwd = it },
                            label = {
                                Text("Contraseña nueva")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.width(200.dp),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                verContraseña2 = !verContraseña2
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(15.dp),
                                painter = painterResource(R.drawable.invisible),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }
                }
                //REPETIR NUEVA CONTRASEÑA
                Row(modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp)) {
                    if(!verContraseña) {
                        TextField(
                            value = nuevaPwd2,
                            onValueChange = { nuevaPwd2 = it },
                            label = {
                                Text("Repetir contraseña nueva")
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.width(200.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                verContraseña3 = !verContraseña3
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(15.dp),
                                painter = painterResource(R.drawable.ojo),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }else{
                        TextField(
                            value = nuevaPwd2,
                            onValueChange = { nuevaPwd2 = it },
                            label = {
                                Text("Repetir contraseña nueva")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.width(200.dp),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                verContraseña3 = !verContraseña3
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                modifier= Modifier.size(15.dp),
                                painter = painterResource(R.drawable.invisible),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }
                }
                Row(modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp)) {
                    Button(
                        onClick = {
                            pwdActualMD5 = encriptarMD5(pwdActual)
                            if (pwdActualMD5 == pwd) {
                                if (nuevaPwd == nuevaPwd2){
                                    var profesor =  Profesor(idProfesor = profesor.idProfesor, pwd = nuevaPwd, dni = profesor.dni,
                                        username = profesor.username, email = profesor.email, rol = profesor.rol, departamento = profesor.departamento,
                                        apellidos = profesor.apellidos, nombre = profesor.nombre)
                                    onActualizarProfesor(profesor)
                                    onPwdChange(nuevaPwd)
                                    onDismiss()
                                }else{
                                    Toast.makeText(contexto, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(contexto, "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.padding(0.dp,0.dp,16.dp,0.dp)
                    ){
                        Text(text = "Aceptar")
                    }
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                    ) {
                        Text(text = "Cancelar")
                    }

                }

            }
        }
    }
}