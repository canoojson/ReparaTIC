package com.example.reparatic.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.reparatic.R
import com.example.reparatic.modelo.Departamento
import com.example.reparatic.modelo.Permiso
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.Rol
import com.example.reparatic.ui.ViewModels.RolUIState


@Composable
fun PantallaInicioRoles(
    uiState: RolUIState,
    onRolesObtenidos: () -> Unit,
    onRolSeleccionado: (Rol) -> Unit,
    onRolEliminado: (Rol) -> Unit,
    rolSeleccionado: Rol?,
    onActualizarRol: (Rol) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is RolUIState.Cargando -> PantallaCargando(modifier = modifier.fillMaxSize())
        is RolUIState.Error -> PantallaError(modifier = modifier.fillMaxSize())
        is RolUIState.ObtenerExito -> Row(modifier = modifier.fillMaxSize()) {
            ListaRoles(
                roles = uiState.roles,
                onRolClick = onRolSeleccionado,
                modifier = Modifier.weight(1f)
            )


            rolSeleccionado?.let {
                EditorRol(
                    rol = it,
                    onActualizarRol = onActualizarRol,
                    onEliminarRol = onRolEliminado,
                    todosLosPermisos = uiState.permisos,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        is RolUIState.ActualizarExito -> onRolesObtenidos()
        is RolUIState.CrearExito -> onRolesObtenidos()
        is RolUIState.EliminarExito -> onRolesObtenidos()
    }
}

@Composable
fun ListaRoles(
    roles: List<Rol>,
    onRolClick: (Rol) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxHeight().padding(8.dp)) {
        items(roles) { rol ->
            Card(
                modifier = Modifier
                    .width(400.dp)
                    .clickable { onRolClick(rol) }
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = rol.descrip,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun EditorRol(
    rol: Rol,
    todosLosPermisos: List<Permiso>,
    onActualizarRol: (Rol) -> Unit,
    onEliminarRol: (Rol) -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by remember(rol.idRol) { mutableStateOf(rol.descrip) }
    var permisosSeleccionados by remember(rol.idRol) {
        mutableStateOf(rol.permisos.map { it.codPermiso }.toSet())
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(0.dp,16.dp,64.dp,16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Editar Rol", style = MaterialTheme.typography.headlineSmall)

        Row() {
            Button(
                onClick = {
                    val nuevosPermisos = todosLosPermisos.filter { permisosSeleccionados.contains(it.codPermiso) }
                    val rolActualizado = rol.copy(descrip = nombre, permisos = nuevosPermisos)
                    onActualizarRol(rolActualizado)
                }
            ) {
                Image(
                    modifier= Modifier.size(20.dp),
                    painter = painterResource(R.drawable.guardar_el_archivo),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Guardar"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onEliminarRol(rol) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(start = 8.dp)
            ){
                Image(
                    modifier= Modifier.size(20.dp),
                    imageVector = Icons.Filled.Delete,
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.eliminar)
                )
            }
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Rol") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Permisos:", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(todosLosPermisos) { permiso ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = permisosSeleccionados.contains(permiso.codPermiso),
                        onCheckedChange = { isChecked ->
                            permisosSeleccionados = if (isChecked) {
                                permisosSeleccionados + permiso.codPermiso
                            } else {
                                permisosSeleccionados - permiso.codPermiso
                            }
                        }
                    )
                    Text(
                        text = permiso.descrip,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun RolDialog(
    onDismiss: () -> Unit,
    onInsertarPulsado: (rol: Rol) -> Unit,
    rol: Rol
) {
    var nombreRol by remember { mutableStateOf(rol.descrip) }
    var rolNuevo by remember { mutableStateOf(Rol(0, nombreRol, emptyList())) }
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
                            onInsertarPulsado(rolNuevo)
                            Toast.makeText(contexto, "Rol insertado correctamente", Toast.LENGTH_SHORT).show()
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.guardar_el_archivo),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Guardar"
                        )
                    }
                }
                TextField(
                    value = nombreRol,
                    onValueChange = { nombreRol = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp),
                    singleLine = true
                )
            }
        }
    }
}

