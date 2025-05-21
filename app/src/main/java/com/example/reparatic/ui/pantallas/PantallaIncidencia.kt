package com.example.reparatic.ui.pantallas

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.reparatic.R
import com.example.reparatic.modelo.Comentario
import com.example.reparatic.modelo.Departamento
import com.example.reparatic.modelo.Estado
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.TiposHw
import com.example.reparatic.modelo.Ubicacion
import com.example.reparatic.ui.DepartamentoUIState
import com.example.reparatic.ui.EstadoUIState
import com.example.reparatic.ui.LoginUIState
import com.example.reparatic.ui.ProfesorUIState
import com.example.reparatic.ui.TiposHwUIState
import com.example.reparatic.ui.UbicacionUIState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaIncidencia(
    incidencia: Incidencia,
    login : Profesor,
    uiStatePro: ProfesorUIState,
    uiStateDep: DepartamentoUIState,
    uiStateEst : EstadoUIState,
    uiStateUbi : UbicacionUIState,
    uiStateTiposHw :  TiposHwUIState,
    onActualizarPulsado: (Incidencia) -> Unit,
    onEliminarPulsado: (id: Int) -> Unit,
    onUbicacionEliminada: (id: Int) -> Unit,
    onUbicacionActualizada: (Ubicacion) -> Unit,
    modifier: Modifier = Modifier
) {
    // Variables modificables de incidencia
    var tipo by remember { mutableStateOf(incidencia.tipo) }
    var ubicacion by remember { mutableStateOf(incidencia.ubicacion) }
    var descripcion by remember { mutableStateOf(incidencia.descripcion) }
    var observaciones by remember { mutableStateOf(incidencia.observaciones) }
    var mas_info by remember { mutableStateOf(incidencia.mas_info) }
    var comentarios by remember { mutableStateOf<List<Comentario>>(emptyList()) }
    var nuevoComentario by remember { mutableStateOf("") }
    var tiempoInvertido by remember { mutableStateOf(incidencia.tiempo_invertido) }
    var fechaIncidencia by remember { mutableStateOf(incidencia.fecha_incidencia) }
    var responsable by remember { mutableStateOf(incidencia.responsable) }
    var departamento by remember { mutableStateOf(incidencia.departamento) }
    var login by remember { mutableStateOf(login) }
    var estado by remember { mutableStateOf(incidencia.estado) }
    var incidenciaHardware by remember { mutableStateOf(incidencia.incidenciaHardware) }
    var incidenciaSoftware by remember { mutableStateOf(incidencia.incidenciaSoftware) }
    var fechaResolucion by remember { mutableStateOf(incidencia.fecha_resolucion) }

    // Remember de listas desplegables
    var expanded by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var expanded3 by remember { mutableStateOf(false) }
    var expanded4 by remember { mutableStateOf(false) }
    var expanded5 by remember { mutableStateOf(false) }
    var expanded6 by remember { mutableStateOf(false) }

    //Estado de TextField y listas desplegables
    var isEditable by remember { mutableStateOf(false) }
    var enModoEdicion by remember { mutableStateOf(false) }

    //Listas
    var listaProfesores = emptyList<Profesor>()
    var listaDepartamentos = emptyList<Departamento>()
    var listaEstados = emptyList<Estado>()
    var listaUbicaciones = emptyList<Ubicacion>()
    var listaTiposHw = emptyList<TiposHw>()
    var opcionesTipo = listOf("HW","SW")

    //Recuperamos los datos de las listas
    when(uiStatePro){
        is ProfesorUIState.ObtenerExito ->
            listaProfesores = uiStatePro.profesores
        else -> PantallaError(modifier= modifier.fillMaxWidth())
    }
    when(uiStateDep){
        is DepartamentoUIState.ObtenerExito ->
            listaDepartamentos = uiStateDep.departamentos
        else -> PantallaError(modifier = modifier.fillMaxWidth())
    }
    when(uiStateEst){
        is EstadoUIState.ObtenerExito ->
            listaEstados = uiStateEst.estados
        else -> PantallaError(modifier = modifier.fillMaxWidth())
    }
    when(uiStateUbi){
        is UbicacionUIState.ObtenerExito ->
            listaUbicaciones = uiStateUbi.ubicaciones
        else -> PantallaError(modifier = modifier.fillMaxWidth())
    }
    when(uiStateTiposHw){
        is TiposHwUIState.ObtenerExito ->
            listaTiposHw = uiStateTiposHw.tiposHw
        else -> PantallaError(modifier = modifier.fillMaxWidth())
    }

    //Dialogos
    var mostrarDialogoUbicacion by remember { mutableStateOf(false) }
    var mostrarDialogoUbicacionDetalles by remember { mutableStateOf(false) }

    val contexto = LocalContext.current

    //Column principal
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row( ) {
                    //Aqui mostramos el boton de Guardar si estamos en el modo de edicion
                    if(enModoEdicion){
                        Button(
                            onClick = {
                                val incidenciaAct = Incidencia(idIncidencia = incidencia.idIncidencia, tipo = tipo, fecha_incidencia = fechaIncidencia,
                                    profesor = incidencia.profesor, departamento = departamento, ubicacion = ubicacion, descripcion = descripcion,
                                    observaciones = observaciones, estado = estado, responsable = responsable, fecha_resolucion = fechaResolucion,
                                    tiempo_invertido = tiempoInvertido, mas_info = mas_info, comentarios = comentarios, incidenciaHardware = incidenciaHardware,
                                    incidenciaSoftware = incidenciaSoftware, fecha_introduccion = incidencia.fecha_introduccion)
                                onActualizarPulsado(incidenciaAct)
                                Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                                isEditable = false
                                enModoEdicion = false
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            Text(text = "Guardar")
                        }
                    //Si no lo estamos, mostramos el de editar

                    }else{
                        Button(
                            onClick = {
                                isEditable = true
                                enModoEdicion = true
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 15.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            Text(text = "Editar")
                        }
                    }
                    Button(onClick = {
                        onEliminarPulsado(incidencia.idIncidencia)
                    },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        ),
                        colors = ButtonColors(Color.Red, Color.Black, Color.Red, Color.Black),
                        modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                    ) {
                        Text(text = "Eliminar")
                    }
                }
                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.width(500.dp)
                        .padding(0.dp, 16.dp, 0.dp, 16.dp),
                    readOnly = !isEditable
                )
                Row {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            if(isEditable){
                                expanded = !expanded
                            }
                        }
                    ) {
                        TextField(
                            value = responsable!!.nombre + " " + responsable!!.apellidos,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Responsable") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            listaProfesores.forEach { profesor ->
                                DropdownMenuItem(
                                    text = { Text(text = profesor.nombre + " " + profesor.apellidos) },
                                    onClick = {
                                        responsable = profesor
                                        expanded = false
                                    },
                                    enabled = isEditable
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = expanded2,
                        onExpandedChange = {
                            if(isEditable){
                                expanded2 = !expanded2
                            }
                        },
                        modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                    ) {
                        TextField(
                            value = departamento!!.nombreDpto,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Departamento") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                        ) {
                            listaDepartamentos.forEach { departamentos ->
                                DropdownMenuItem(
                                    text = { Text(text = departamentos.nombreDpto) },
                                    onClick = {
                                        departamento = departamentos
                                        expanded2 = false
                                    },
                                    enabled = isEditable
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = expanded3,
                        onExpandedChange = {
                            if(isEditable){
                                expanded3 = !expanded3
                            }
                        },
                        modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                    ) {
                        TextField(
                            value = estado!!.descrip,
                            onValueChange = {estado!!.descrip = it},
                            readOnly = true,
                            label = { Text("Estado") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded3) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded3,
                            onDismissRequest = { expanded3 = false },
                        ) {
                            listaEstados.forEach { estados ->
                                DropdownMenuItem(
                                    text = { Text(text = estados.descrip) },
                                    onClick = {
                                        estado = estados
                                        expanded3 = false
                                    },
                                    enabled = isEditable
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = expanded5,
                        onExpandedChange = {
                            if(isEditable){
                                expanded5 = !expanded5
                            }
                        },
                        modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                    ) {
                        TextField(
                            value = tipo,
                            onValueChange = { tipo = it},
                            readOnly = true,
                            label = { Text("Tipo de Incidencia") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded5) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded5,
                            onDismissRequest = { expanded5 = false },
                        ) {
                            opcionesTipo.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(text = opcion) },
                                    onClick = {
                                        tipo = opcion
                                        expanded5 = false
                                    },
                                    enabled = isEditable
                                )
                            }
                        }
                    }
                }
                if(estado!!.descrip=="Resuelta"){
                    Row {
                        TextField(
                            value = fechaResolucion!!,
                            onValueChange = { fechaResolucion = it },
                            readOnly = true,
                            label = { Text("Fecha de resolución") },
                            modifier = Modifier.width(150.dp)
                        )
                        DatePickerWithFormattedString { date ->
                            fechaResolucion = date
                        }
                    }
                }
                if(observaciones==null){
                    observaciones= ""
                }
                TextField(
                    value = observaciones!!,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth()
                        .height(100.dp),
                    readOnly = !isEditable
                )
                Row {
                    ExposedDropdownMenuBox(
                        expanded = expanded4,
                        onExpandedChange = {
                            if(isEditable){
                                expanded4 = !expanded4
                            }
                        }
                    ) {
                        TextField(
                            value = ubicacion!!.nombre,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ubicacion") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded4) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded4,
                            onDismissRequest = { expanded4 = false },
                        ) {
                            listaUbicaciones.forEach { ubicaciones ->
                                DropdownMenuItem(
                                    text = { Text(text = ubicaciones.nombre) },
                                    onClick = {
                                        ubicacion = ubicaciones
                                        expanded4 = false
                                    },
                                    enabled = isEditable
                                )
                            }
                        }
                    }
                    if(isEditable){
                        Button(
                            onClick = {
                                mostrarDialogoUbicacion = true
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.enlace_roto_),
                                contentDescription = "Editar ubicación",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }else{
                        Button(
                            onClick = {
                                mostrarDialogoUbicacionDetalles = true
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent, Color.Transparent,Color.Transparent),
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.enlace_roto),
                                contentDescription = "Editar ubicación",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                Row{
                    if(tipo == "HW"){
                        TextField(
                            value = if(incidenciaHardware!=null) incidenciaHardware!!.modelo else "",
                            onValueChange = {incidenciaHardware!!.modelo = it},
                            readOnly = !enModoEdicion,
                            label = { Text("Modelo") },
                            modifier = Modifier.width(200.dp)
                                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                        )
                        TextField(
                            value = if(incidenciaHardware!=null) incidenciaHardware!!.numSerie else "",
                            onValueChange = {incidenciaHardware!!.numSerie = it},
                            readOnly = !enModoEdicion,
                            label = { Text("Número de serie") },
                            modifier = Modifier
                                .padding(16.dp, 16.dp,0.dp,0.dp)
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded6,
                            onExpandedChange = {
                                if(isEditable){
                                    expanded6 = !expanded6
                                }
                            },
                            modifier = Modifier.padding(16.dp,16.dp,0.dp,0.dp)
                        ) {
                            TextField(
                                value = if(incidenciaHardware!=null) incidenciaHardware!!.tipoHw.descrip else "",
                                onValueChange = {incidenciaHardware!!.tipoHw.descrip = it},
                                readOnly = true,
                                label = { Text("Tipo Hardware") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded6) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded6,
                                onDismissRequest = { expanded6 = false },
                            ) {
                                listaTiposHw.forEach { tiposHw ->
                                    DropdownMenuItem(
                                        text = { Text(text = tiposHw.descrip) },
                                        onClick = {
                                            incidenciaHardware!!.tipoHw = tiposHw
                                            expanded6 = false
                                        },
                                        enabled = isEditable
                                    )
                                }
                            }
                        }
                    }else{
                        TextField(
                            value = if(incidenciaSoftware!=null)incidenciaSoftware!!.software else "",
                            onValueChange = {incidenciaSoftware!!.software = it},
                            readOnly = !enModoEdicion,
                            label = { Text("Software") },
                            modifier = Modifier.width(200.dp)
                                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                        )
                        TextField(
                            value = if(incidenciaSoftware!=null) incidenciaSoftware!!.software else "",
                            onValueChange = {incidenciaSoftware!!.software = it},
                            readOnly = !enModoEdicion,
                            label = { Text("Clave") },
                            modifier = Modifier
                                .padding(16.dp, 16.dp,0.dp,0.dp)
                        )
                        TextField(
                            value = if(incidenciaSoftware!=null)incidenciaSoftware!!.SO else "",
                            onValueChange = {incidenciaSoftware!!.SO = it},
                            readOnly = !enModoEdicion,
                            label = { Text("S.O") },
                            modifier = Modifier
                                .padding(16.dp, 16.dp,0.dp,0.dp)
                        )
                    }
                }

                /*
                if (mas_info != null){
                    if(detectarTipoArchivo(mas_info!!)=="imagen/jpeg" || detectarTipoArchivo(mas_info!!)=="imagen/png"){
                        val imagenConvertida = remember(mas_info) {
                            byteArrayToBitmap(mas_info!!)
                        }
                        Image(
                            bitmap = imagenConvertida.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }else if(detectarTipoArchivo(mas_info!!)=="application/pdf"){
                        val archivoPdf = remember { guardarPdfTemporal(contexto, mas_info!!) }
                        Button(onClick = {
                            // Abrir PDF con un Intent
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(
                                    FileProvider.getUriForFile(contexto, "${contexto.packageName}.fileprovider", archivoPdf),
                                    "application/pdf"
                                )
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            contexto.startActivity(intent)
                        }) {
                            Text("Abrir PDF")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            // Descargar: copiar archivo a Descargas o mostrar diálogo para guardar
                            // Aquí solo mostramos ejemplo de copiar a Descargas
                            val descargaDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            val destino = File(descargaDir, archivoPdf.name)
                            archivoPdf.copyTo(destino, overwrite = true)
                            Toast.makeText(contexto, "PDF guardado en Descargas", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Descargar PDF")
                        }
                    }
                }*/
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            if(incidencia.comentarios!=null && comentarios.isEmpty()){
                comentarios = incidencia.comentarios
            }
            comentarios.forEach{ comentario ->
                Column(modifier = modifier.fillMaxWidth()
                    .padding(16.dp)){
                    Text(text = comentario.profesor!!.nombre + " " + comentario.profesor.apellidos,
                        fontWeight = FontWeight.Bold)
                    Text(text = comentario.comentario!!.toString())
                    Text(text = formatearFecha(comentario.fecha!!),
                        color = Color.Gray,
                        fontSize = 12.sp)
                }
            }

            // HACER TRAS LA PANTALLA DE INICIO DE SESION

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = nuevoComentario,
                    onValueChange = { nuevoComentario = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Escribe un comentario...") },
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (nuevoComentario.isNotBlank()) {
                            Log.v("ReparaTICApp", login.toString())
                            comentarios = comentarios + Comentario(
                                profesor = login,
                                comentario = nuevoComentario,
                                fecha = getFechaActual()
                            )
                            nuevoComentario = ""
                            val incidenciaAct = Incidencia(idIncidencia = incidencia.idIncidencia, tipo = tipo, fecha_incidencia = fechaIncidencia,
                                profesor = incidencia.profesor, departamento = departamento, ubicacion = ubicacion, descripcion = descripcion,
                                observaciones = observaciones, estado = estado, responsable = responsable, fecha_resolucion = fechaResolucion,
                                tiempo_invertido = tiempoInvertido, mas_info = mas_info, comentarios = comentarios, incidenciaHardware = incidenciaHardware,
                                incidenciaSoftware = incidenciaSoftware, fecha_introduccion = incidencia.fecha_introduccion)
                            onActualizarPulsado(incidenciaAct)
                        }
                    }
                ) {
                    Text("Enviar")
                }
            }
        }
    }

    if(mostrarDialogoUbicacion){
        DialogoUbicacion(onDismiss = {mostrarDialogoUbicacion = false}, ubicacion = ubicacion!!,
            onUbicacionActualizada = onUbicacionActualizada, onUbicacionEliminada = onUbicacionEliminada)
    }
    if(mostrarDialogoUbicacionDetalles){
        DialogoUbicacionDetalles(onDismiss = {mostrarDialogoUbicacionDetalles = false}, ubicacion = ubicacion!!)
    }
}


@Composable
fun DialogoUbicacion(onDismiss: () -> Unit, ubicacion: Ubicacion,
                     onUbicacionActualizada: (Ubicacion) -> Unit,
                     onUbicacionEliminada:(id: Int) -> Unit){
    var nombre by remember { mutableStateOf(ubicacion.nombre) }
    var detalles by remember { mutableStateOf(ubicacion.descrip) }
    val contexto = LocalContext.current
    val ubicacionActualizada = Ubicacion(idUbicacion = ubicacion.idUbicacion, nombre = nombre, descrip = detalles)
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
                            Log.v("Dialog", ubicacionActualizada.toString())
                            onUbicacionActualizada(ubicacionActualizada)
                            Toast.makeText(contexto, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(text = "Guardar")
                    }
                    Button(onClick = {
                        Log.v("Ubicacion Eliminada", ubicacion.idUbicacion.toString())
                        onUbicacionEliminada(ubicacion.idUbicacion)
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
                        onClick = onDismiss,
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
