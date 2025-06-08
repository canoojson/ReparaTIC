package com.example.reparatic.ui.pantallas

import android.util.Log
 import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reparatic.R
import com.example.reparatic.modelo.Comentario
import com.example.reparatic.modelo.Departamento
import com.example.reparatic.modelo.Estado
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.IncidenciaHardware
import com.example.reparatic.modelo.IncidenciaSoftware
import com.example.reparatic.modelo.Permiso
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.TiposHw
import com.example.reparatic.modelo.Ubicacion
import com.example.reparatic.ui.DatePickerWithFormattedString
import com.example.reparatic.ui.TimePickerWithFormattedString
import com.example.reparatic.ui.ViewModels.DepartamentoUIState
import com.example.reparatic.ui.ViewModels.EstadoUIState
import com.example.reparatic.ui.ViewModels.ProfesorUIState
import com.example.reparatic.ui.ViewModels.TiposHwUIState
import com.example.reparatic.ui.ViewModels.UbicacionUIState
import com.example.reparatic.ui.esFechaValida
import com.example.reparatic.ui.formatearFecha
import com.example.reparatic.ui.getFechaActual


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaIncidencia(
    incidencia: Incidencia,
    login : Profesor,
    uiStatePro: ProfesorUIState,
    uiStateDep: DepartamentoUIState,
    uiStateEst : EstadoUIState,
    uiStateUbi : UbicacionUIState,
    uiStateTiposHw : TiposHwUIState,
    onInsertarPulsado: (Incidencia) -> Unit,
    onActualizarPulsado: (Incidencia) -> Unit,
    onEliminarPulsado: (id: Int) -> Unit,
    onIncidenciaSoftwareEliminada : (IncidenciaSoftware) -> Unit,
    onIncidenciaHardwareEliminada : (IncidenciaHardware) -> Unit,
    modifier: Modifier = Modifier
) {
    // Variables modificables de incidencia
    var idIncidencia by remember { mutableIntStateOf(incidencia.idIncidencia) }
    var tipo by remember { mutableStateOf(incidencia.tipo) }
    var ubicacion by remember { mutableStateOf(incidencia.ubicacion) }
    var descripcion by remember { mutableStateOf(incidencia.descripcion) }
    var observaciones by remember { mutableStateOf(incidencia.observaciones) }
    var mas_info by remember { mutableStateOf(incidencia.mas_info) }
    var comentarios by remember {
        if(incidencia.comentarios.isNullOrEmpty()){
            mutableStateOf(emptyList<Comentario>())
        }else{
            mutableStateOf(incidencia.comentarios)
        }
    }
    var nuevoComentario by remember { mutableStateOf("") }
    var tiempoInvertido by remember { mutableStateOf(incidencia.tiempo_invertido) }
    var fechaIncidencia by remember { mutableStateOf(incidencia.fecha_incidencia) }
    if(fechaIncidencia=="" || fechaIncidencia==null){
        fechaIncidencia = formatearFecha(getFechaActual())
    }
    var responsable by remember { mutableStateOf(incidencia.responsable) }
    var departamento by remember { mutableStateOf(incidencia.departamento) }
    var estado by remember { mutableStateOf(incidencia.estado) }
    var incidenciaHardware by remember { mutableStateOf(incidencia.incidenciaHardware) }
    var incidenciaSoftware by remember { mutableStateOf(incidencia.incidenciaSoftware) }
    var modelo by remember { mutableStateOf(incidenciaHardware?.modelo?: "") }
    var numserie by remember { mutableStateOf(incidenciaHardware?.numSerie?: "") }
    var tipoHw by remember { mutableStateOf(incidenciaHardware?.tipoHw) }
    var software by remember { mutableStateOf(incidenciaSoftware?.software?: "") }
    var clave by remember { mutableStateOf(incidenciaSoftware?.clave?: "") }
    var so by remember { mutableStateOf(incidenciaSoftware?.SO?: "") }
    var fechaResolucion by remember { mutableStateOf(incidencia.fecha_resolucion) }
    var errorFecha by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    //Permisos
    val permiso = Permiso(codPermiso = 2, descrip = "Modificar/Borrar incidencias")
    val permiso2 = Permiso(codPermiso = 1, descrip = "Añadir incidencias")

    // Remember de listas desplegables
    var expanded by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var expanded3 by remember { mutableStateOf(false) }
    var expanded4 by remember { mutableStateOf(false) }
    var expanded5 by remember { mutableStateOf(false) }
    var expanded6 by remember { mutableStateOf(false) }

    //Estado de TextField y listas desplegables
    var isEditable by remember {
        if(idIncidencia==0){
            mutableStateOf(true)
        }else{
            mutableStateOf(false)
        }}
    var enModoEdicion by remember { if(idIncidencia==0){
        mutableStateOf(true)
    }else{
        mutableStateOf(false)
    } }

    //Listas
    var listaProfesores = emptyList<Profesor>()
    var listaDepartamentos = emptyList<Departamento>()
    var listaEstados = emptyList<Estado>()
    var listaUbicaciones by remember { mutableStateOf(emptyList<Ubicacion>()) }
    var listaTiposHw = emptyList<TiposHw>()
    var opcionesTipo = listOf("HW","SW")

    //Recuperamos los datos de las listas
    when(uiStatePro){
        is ProfesorUIState.ObtenerExito ->
            listaProfesores = uiStatePro.profesoresInformatica
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
    LaunchedEffect(uiStateUbi) {
        when(uiStateUbi){
            is UbicacionUIState.ObtenerExito ->
                listaUbicaciones = uiStateUbi.ubicaciones
            is UbicacionUIState.ActualizarExito ->
                ubicacion = uiStateUbi.ubicacion.body()
            is UbicacionUIState.Error -> {}

            else -> {

            }
        }
    }
    when(uiStateTiposHw){
        is TiposHwUIState.ObtenerExito ->
            listaTiposHw = uiStateTiposHw.tiposHw
        else -> PantallaError(modifier = modifier.fillMaxWidth())
    }

    //Dialogos
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
                    if(login.rol!!.permisos.isNotEmpty()){
                        if(login.rol.permisos.contains(permiso) || login.idProfesor == (incidencia.profesor?.idProfesor
                                ?: 0)
                        ){
                            if(enModoEdicion){
                                Button(
                                    onClick = {
                                        if(idIncidencia==0){
                                            var incidenciaNueva : Incidencia
                                            incidenciaHardware = IncidenciaHardware(idh = 0, modelo = modelo, numSerie = numserie, tipoHw = tipoHw)
                                            incidenciaSoftware = IncidenciaSoftware(ids = 0, software = software, clave = clave, SO = so)
                                            if(tipo == "HW"){

                                                incidenciaNueva = Incidencia(
                                                    idIncidencia = 0,
                                                    tipo =tipo,
                                                    fecha_incidencia = fechaIncidencia,
                                                    fecha_introduccion = formatearFecha(
                                                        getFechaActual()
                                                    ), profesor = login, departamento = departamento, ubicacion = ubicacion,
                                                    descripcion = descripcion, observaciones = observaciones, estado = estado, responsable = responsable, fecha_resolucion = fechaResolucion,
                                                    tiempo_invertido = tiempoInvertido,
                                                    mas_info = null, comentarios = emptyList(), incidenciaHardware = incidenciaHardware, incidenciaSoftware = null)
                                            }else{
                                                incidenciaNueva = Incidencia(
                                                    idIncidencia = 0,
                                                    tipo =tipo,
                                                    fecha_incidencia = fechaIncidencia,
                                                    fecha_introduccion = formatearFecha(
                                                        getFechaActual()
                                                    ), profesor = login, departamento = departamento, ubicacion = ubicacion,
                                                    descripcion = descripcion, observaciones = observaciones, estado = estado, responsable = responsable, fecha_resolucion = fechaResolucion,
                                                    tiempo_invertido = tiempoInvertido,
                                                    mas_info = null, comentarios = emptyList(), incidenciaHardware = null, incidenciaSoftware = incidenciaSoftware)
                                            }
                                            if(descripcion == ""|| descripcion.length>80|| (observaciones?.length ?:0) >400|| errorFecha || ubicacion == null
                                                || tipo == "" || (modelo == "" || numserie == "" || tipoHw == null) && (software == "" || clave == "" || so == "" )){
                                                error = true
                                            }else{
                                                if(tipo == "HW"){
                                                    if(incidenciaSoftware!=null){
                                                        onIncidenciaSoftwareEliminada(incidenciaSoftware!!)
                                                    }
                                                }else{
                                                    if(incidenciaHardware!=null){
                                                        onIncidenciaHardwareEliminada(incidenciaHardware!!)
                                                    }
                                                }
                                                onInsertarPulsado(incidenciaNueva)
                                                Toast.makeText(contexto, "Incidencia guardada correctamente", Toast.LENGTH_SHORT).show()
                                                isEditable = false
                                                enModoEdicion = false
                                            }
                                        }else{
                                            var incidenciaActu : Incidencia
                                            if(tipo == "HW"){
                                                incidenciaHardware = IncidenciaHardware(idh = incidencia.incidenciaHardware?.idh?: 0, modelo = modelo, numSerie = numserie, tipoHw = tipoHw)
                                                incidenciaActu = Incidencia(
                                                    idIncidencia = incidencia.idIncidencia,
                                                    tipo =tipo,
                                                    fecha_incidencia = fechaIncidencia,
                                                    fecha_introduccion = formatearFecha(
                                                        getFechaActual()
                                                    ), profesor = incidencia.profesor, departamento = departamento, ubicacion = ubicacion,
                                                    descripcion = descripcion, observaciones = observaciones, estado = estado, responsable = responsable,
                                                    fecha_resolucion = fechaResolucion, tiempo_invertido = tiempoInvertido,
                                                    mas_info = null, comentarios = emptyList(), incidenciaHardware = incidenciaHardware, incidenciaSoftware = null)
                                            }else{
                                                incidenciaSoftware = IncidenciaSoftware(ids = incidencia.incidenciaSoftware?.ids?: 0, software = software, clave = clave, SO = so)
                                                incidenciaActu = Incidencia(
                                                    idIncidencia = incidencia.idIncidencia,
                                                    tipo =tipo,
                                                    fecha_incidencia = fechaIncidencia,
                                                    fecha_introduccion = formatearFecha(
                                                        getFechaActual()
                                                    ), profesor = incidencia.profesor, departamento = departamento, ubicacion = ubicacion,
                                                    descripcion = descripcion, observaciones = observaciones, estado = estado, responsable = responsable, fecha_resolucion = fechaResolucion,
                                                    tiempo_invertido = tiempoInvertido,
                                                    mas_info = null, comentarios = emptyList(), incidenciaHardware = null, incidenciaSoftware = incidenciaSoftware)
                                            }
                                            if(descripcion == ""||descripcion.length>80||(observaciones?.length ?:0) >400||errorFecha || ubicacion == null
                                                || tipo == "" || (modelo == "" || numserie == "" || tipoHw == null) && (software == "" || clave == "" || so == "" )){
                                                error = true
                                            }else{
                                                if(tipo == "HW"){
                                                    if(incidenciaSoftware!=null){
                                                        onIncidenciaSoftwareEliminada(incidenciaSoftware!!)
                                                    }
                                                }else{
                                                    if(incidenciaHardware!=null){
                                                        onIncidenciaHardwareEliminada(incidenciaHardware!!)
                                                    }
                                                }
                                                onActualizarPulsado(incidenciaActu)
                                                Toast.makeText(contexto, "Incidencia actualizada correctamente", Toast.LENGTH_SHORT).show()
                                                isEditable = false
                                                enModoEdicion = false
                                            }
                                        }
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
                                    Image(
                                        modifier= Modifier.size(20.dp),
                                        imageVector = Icons.Default.Edit,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Editar"
                                    )
                                }
                            }
                            if(incidencia.idIncidencia!=0){
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
                                    Image(
                                        modifier= Modifier.size(20.dp),
                                        imageVector = Icons.Filled.Delete,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = stringResource(R.string.eliminar)
                                    )
                                }
                            }
                        }else{
                            if(login.rol.permisos.isNotEmpty() && login.rol.permisos.contains(permiso2)){
                                if(idIncidencia==0) {
                                    Button(
                                        onClick = {
                                            var incidenciaNueva: Incidencia
                                            incidenciaHardware = IncidenciaHardware(
                                                idh = 0,
                                                modelo = modelo,
                                                numSerie = numserie,
                                                tipoHw = tipoHw
                                            )
                                            incidenciaSoftware = IncidenciaSoftware(
                                                ids = 0,
                                                software = software,
                                                clave = clave,
                                                SO = so
                                            )
                                            if (tipo == "HW") {

                                                incidenciaNueva = Incidencia(
                                                    idIncidencia = 0,
                                                    tipo = tipo,
                                                    fecha_incidencia = fechaIncidencia,
                                                    fecha_introduccion = formatearFecha(
                                                        getFechaActual()
                                                    ),
                                                    profesor = login,
                                                    departamento = departamento,
                                                    ubicacion = ubicacion,
                                                    descripcion = descripcion,
                                                    observaciones = observaciones,
                                                    estado = estado,
                                                    responsable = responsable,
                                                    fecha_resolucion = fechaResolucion,
                                                    tiempo_invertido = tiempoInvertido,
                                                    mas_info = null,
                                                    comentarios = emptyList(),
                                                    incidenciaHardware = incidenciaHardware,
                                                    incidenciaSoftware = null
                                                )
                                            } else {
                                                incidenciaNueva = Incidencia(
                                                    idIncidencia = 0,
                                                    tipo = tipo,
                                                    fecha_incidencia = fechaIncidencia,
                                                    fecha_introduccion = formatearFecha(
                                                        getFechaActual()
                                                    ),
                                                    profesor = login,
                                                    departamento = departamento,
                                                    ubicacion = ubicacion,
                                                    descripcion = descripcion,
                                                    observaciones = observaciones,
                                                    estado = estado,
                                                    responsable = responsable,
                                                    fecha_resolucion = fechaResolucion,
                                                    tiempo_invertido = tiempoInvertido,
                                                    mas_info = null,
                                                    comentarios = emptyList(),
                                                    incidenciaHardware = null,
                                                    incidenciaSoftware = incidenciaSoftware
                                                )
                                            }
                                            if(descripcion == ""||descripcion.length>80||(observaciones?.length ?:0) >400||errorFecha|| ubicacion == null
                                                || tipo == "" || (modelo == "" || numserie == "" || tipoHw == null) && (software == "" || clave == "" || so == "" )){
                                                error = true
                                            }else{
                                                if(tipo == "HW"){
                                                    if(incidenciaSoftware!=null){
                                                        onIncidenciaSoftwareEliminada(incidenciaSoftware!!)
                                                    }
                                                }else{
                                                    onIncidenciaHardwareEliminada(incidenciaHardware!!)
                                                }
                                                onInsertarPulsado(incidenciaNueva)
                                                Toast.makeText(contexto, "Incidencia guardada correctamente", Toast.LENGTH_SHORT).show()
                                                isEditable = false
                                                enModoEdicion = false
                                            }
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
                            }
                        }
                    }
                }
                Row {
                    TextField(
                        value = if(incidencia.idIncidencia==0) "Auto" else incidencia.idIncidencia.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("ID") },
                        modifier = Modifier.width(150.dp)
                            .padding(0.dp, 16.dp, 16.dp, 16.dp)
                    )
                    TextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                        },
                        isError =
                            if(descripcion=="" || descripcion.length>80){
                                error
                            }else{
                                false
                            },
                        singleLine = true,
                        supportingText = {
                            Text(text = descripcion.length.toString() + "/80" + "       " +
                                    if(descripcion.length>80) "Máximo de caracteres alcanzado" else if(descripcion==""&&error) "Rellena este campo antes de continuar" else "") },
                        label = { Text("Descripción") },
                        modifier = Modifier.width(500.dp)
                            .padding(0.dp, 16.dp, 16.dp, 16.dp),
                        readOnly = !enModoEdicion
                    )
                    TextField(
                        value = fechaIncidencia?: formatearFecha(getFechaActual()),
                        onValueChange = {},
                        isError = errorFecha,
                        supportingText = {Text(text =
                                if(errorFecha) "No puedes introducir una fecha futura" else if(fechaIncidencia==""&&errorFecha)
                                    "Rellena este campo antes de continuar" else "")},
                        readOnly = true,
                        label = { Text("Fecha incidencia") },
                        modifier = Modifier.width(200.dp).padding(0.dp, 16.dp, 0.dp, 16.dp)
                    )
                    if(enModoEdicion){
                        DatePickerWithFormattedString{ fecha ->
                            val fechaFormateada = formatearFecha(fecha)
                            Log.d("fecha", fechaFormateada)
                            fechaIncidencia = fechaFormateada
                            errorFecha = !esFechaValida(fechaFormateada)
                            Log.d("e", errorFecha.toString())
                        }
                    }

                }


                    Row {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                if (enModoEdicion && (login.rol?.descrip=="Coordinador TIC" || login.rol?.descrip=="Directivo" || login.rol?.descrip=="Administrador")) {
                                    expanded = !expanded
                                }
                            }
                        ) {
                            TextField(
                                value = (responsable?.nombre ?: "") + " " + (responsable?.apellidos ?: ""),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Responsable") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .heightIn(max = 400.dp)
                            ) {
                                listaProfesores.forEach { profesor ->
                                    DropdownMenuItem(
                                        text = { Text(text = profesor.nombre + " " + profesor.apellidos) },
                                        onClick = {
                                            responsable = profesor
                                            expanded = false
                                        },
                                        enabled = enModoEdicion
                                    )
                                }
                            }
                        }
                        ExposedDropdownMenuBox(
                            expanded = expanded3,
                            onExpandedChange = {
                                if(enModoEdicion && (login.rol?.descrip == "Coordinador TIC" || login.rol?.descrip == "Directivo" || login.rol?.descrip == "Administrador" || (incidencia.responsable?.idProfesor
                                        ?: 0) == login.idProfesor)
                                ){
                                    expanded3 = !expanded3
                                }
                            },
                            modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                        ) {
                            TextField(
                                value = estado?.descrip?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Estado") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded3) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded3,
                                onDismissRequest = { expanded3 = false },
                                modifier = Modifier
                                    .heightIn(max = 400.dp)
                            ) {
                                listaEstados.forEach { estados ->
                                    DropdownMenuItem(
                                        text = { Text(text = estados.descrip) },
                                        onClick = {
                                            estado = estados
                                            expanded3 = false
                                        },
                                        enabled = enModoEdicion
                                    )
                                }
                            }
                        }
                        ExposedDropdownMenuBox(
                            expanded = expanded2,
                            onExpandedChange = {
                                if(enModoEdicion){
                                    expanded2 = !expanded2
                                }
                            },
                            modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp)
                        ) {
                            TextField(
                                value = departamento?.nombreDpto?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Departamento afectado") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded2,
                                onDismissRequest = { expanded2 = false },
                                modifier = Modifier
                                    .heightIn(max = 400.dp)
                            ) {

                                listaDepartamentos.forEach { departamentos ->
                                    DropdownMenuItem(
                                        text = { Text(text = departamentos.nombreDpto) },
                                        onClick = {
                                            departamento = departamentos
                                            expanded2 = false
                                        },
                                        enabled = enModoEdicion
                                    )
                                }

                            }
                        }
                    }
                }
                if((estado?.descrip?: "")=="Solucionada" && (login.rol?.descrip == "Coordinador TIC" || login.rol?.descrip == "Directivo" || login.rol?.descrip == "Administrador" || (incidencia.responsable?.idProfesor
                        ?: 0) == login.idProfesor)){
                    Row {
                        TextField(
                            value = formatearFecha(getFechaActual()),
                            onValueChange = { fechaResolucion = it },
                            readOnly = true,
                            label = { Text("Fecha de resolución") },
                            modifier = Modifier.width(150.dp)
                        )
                    }
                }
                if(observaciones==null){
                    observaciones= ""
                }
                TextField(
                    value = observaciones!!,
                    onValueChange = { observaciones = it },
                    isError= if((observaciones?.length?:0)>400){
                        error
                    }else{
                        false
                    },
                    maxLines = 4,
                    label = { Text("Observaciones") },
                    supportingText = {Text(text = observaciones?.length.toString() + "/400" + "       " +
                            if((observaciones?.length?:0)>400) "Máximo de caracteres alcanzado" else "")},
                    modifier = Modifier.fillMaxWidth()
                        .height(150.dp)
                        .padding(16.dp, 0.dp, 0.dp, 16.dp),
                    readOnly = !enModoEdicion
                )
                Row {
                    ExposedDropdownMenuBox(
                        expanded = expanded4,
                        onExpandedChange = {
                            if(enModoEdicion){
                                expanded4 = !expanded4
                            }
                        },
                        modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp)
                    ) {
                        TextField(
                            value = ubicacion?.nombre ?:"",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ubicacion") },
                            isError = if(ubicacion==null){
                                error
                            }else{
                                false
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded4) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded4,
                            onDismissRequest = { expanded4 = false },
                            modifier = Modifier
                                .heightIn(max = 400.dp)
                        ) {
                            listaUbicaciones.forEach { ubicaciones ->
                                DropdownMenuItem(
                                    text = { Text(text = ubicaciones.nombre) },
                                    onClick = {
                                        ubicacion = ubicaciones
                                        expanded4 = false
                                    },
                                    enabled = enModoEdicion
                                )
                            }
                        }
                    }
                    if (ubicacion != null){
                        Button(
                            onClick = {
                                mostrarDialogoUbicacionDetalles = true
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
                    }
                    ExposedDropdownMenuBox(
                        expanded = expanded5,
                        onExpandedChange = {
                            if(enModoEdicion){
                                expanded5 = !expanded5
                            }
                        },
                        modifier = Modifier.padding(0.dp,0.dp,16.dp,16.dp)
                    ) {
                        TextField(
                            value = tipo,
                            onValueChange = {},
                            readOnly = true,
                            isError = if (tipo == ""){
                                error
                            }else{
                                false
                            },
                            label = { Text("Tipo de Incidencia") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded5) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded5,
                            onDismissRequest = { expanded5 = false },
                            modifier = Modifier
                                .heightIn(max = 400.dp)
                        ) {
                            opcionesTipo.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(text = opcion) },
                                    onClick = {
                                        tipo = opcion
                                        expanded5 = false
                                    },
                                    enabled = enModoEdicion
                                )
                            }
                        }
                    }
                    if(estado?.descrip == "Solucionada" && (login.rol?.descrip == "Coordinador TIC" || login.rol?.descrip == "Directivo" || login.rol?.descrip == "Administrador")
                        || (responsable?.idProfesor ?: 0) == login.idProfesor
                    ) {
                        TextField(
                            value = tiempoInvertido ?: "00:00:00",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tiempo invertido") },
                            modifier = Modifier.width(150.dp)
                        )
                        if (enModoEdicion) {
                            TimePickerWithFormattedString { hora ->
                                tiempoInvertido = hora
                            }
                        }
                    }
                }
                Row{
                    if(tipo == "HW"){
                        TextField(
                            value = modelo,
                            onValueChange = {modelo = it},
                            readOnly = !enModoEdicion,
                            isError = if(modelo==""){
                                error
                            }else{
                                false
                            },
                            label = { Text("Modelo") },
                            modifier = Modifier.width(200.dp)
                                .padding(0.dp, 16.dp, 0.dp, 16.dp),
                            singleLine = true
                        )
                        TextField(
                            value = numserie,
                            onValueChange = {numserie = it},
                            readOnly = !enModoEdicion,
                            isError = if(numserie==""){
                                error
                            }else{
                                false
                            },
                            label = { Text("Número de serie") },
                            modifier = Modifier
                                .padding(16.dp, 16.dp,0.dp,0.dp),
                            singleLine = true
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded6,
                            onExpandedChange = {
                                if(enModoEdicion){
                                    expanded6 = !expanded6
                                }
                            },
                            modifier = Modifier.padding(16.dp,16.dp,0.dp,0.dp)
                        ) {
                            TextField(
                                value = tipoHw?.descrip ?: "",
                                onValueChange = {  },
                                readOnly = true,
                                isError = if(tipoHw==null){
                                    error
                                }else{
                                    false
                                },
                                label = { Text("Tipo Hardware") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded6) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded6,
                                onDismissRequest = { expanded6 = false },
                                modifier = Modifier
                                    .heightIn(max = 400.dp)
                            ) {
                                listaTiposHw.forEach { tiposHw ->
                                    DropdownMenuItem(
                                        text = { Text(text = tiposHw.descrip) },
                                        onClick = {
                                            tipoHw = tiposHw
                                            expanded6 = false
                                        },
                                        enabled = enModoEdicion
                                    )
                                }
                            }
                        }
                    }else if(tipo == "SW"){
                        TextField(
                            value = software,
                            onValueChange = {software = it},
                            readOnly = !enModoEdicion,
                            isError = if(software==""){
                                error
                            }else{
                                false
                            },
                            label = { Text("Software") },
                            modifier = Modifier.width(200.dp)
                                .padding(0.dp, 16.dp, 0.dp, 16.dp),
                            singleLine = true
                        )
                        TextField(
                            value = clave,
                            onValueChange = { clave = it },
                            readOnly = !enModoEdicion,
                            isError = if(clave==""){
                                error
                            }else{
                                false
                            },
                            label = { Text("Clave") },
                            modifier = Modifier
                                .padding(16.dp, 16.dp,0.dp,0.dp),
                            singleLine = true
                        )
                        TextField(
                            value = so,
                            onValueChange = {so = it},
                            readOnly = !enModoEdicion,
                            isError = if(so==""){
                                error
                            }else{
                                false
                            },
                            label = { Text("S.O") },
                            modifier = Modifier
                                .padding(16.dp, 16.dp,0.dp,0.dp),
                            singleLine = true
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            if(comentarios.isEmpty()){
                Log.v("ENTRO AQUI","SISIS")
                comentarios = emptyList()
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

            if(idIncidencia!=0) {
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
                                comentarios = comentarios + Comentario(
                                    profesor = login,
                                    comentario = nuevoComentario,
                                    fecha = getFechaActual()
                                )
                                nuevoComentario = ""
                                if (idIncidencia == 0) {
                                    var incidenciaNueva: Incidencia

                                    if (tipo == "HW") {
                                        incidenciaHardware = IncidenciaHardware(
                                            idh = 0,
                                            modelo = modelo,
                                            numSerie = numserie,
                                            tipoHw = tipoHw
                                        )
                                        incidenciaSoftware = IncidenciaSoftware(
                                            ids = 0,
                                            software = software,
                                            clave = clave,
                                            SO = so
                                        )

                                        incidenciaNueva = Incidencia(
                                            idIncidencia = 0,
                                            tipo = tipo,
                                            fecha_incidencia = fechaIncidencia,
                                            fecha_introduccion = formatearFecha(getFechaActual()),
                                            profesor = login,
                                            departamento = departamento,
                                            ubicacion = ubicacion,
                                            descripcion = descripcion,
                                            observaciones = observaciones,
                                            estado = estado,
                                            responsable = responsable,
                                            fecha_resolucion = fechaResolucion,
                                            tiempo_invertido = tiempoInvertido,
                                            mas_info = null,
                                            comentarios = emptyList(),
                                            incidenciaHardware = incidenciaHardware,
                                            incidenciaSoftware = null
                                        )
                                    } else {

                                        incidenciaNueva = Incidencia(
                                            idIncidencia = 0,
                                            tipo = tipo,
                                            fecha_incidencia = fechaIncidencia,
                                            fecha_introduccion = formatearFecha(getFechaActual()),
                                            profesor = login,
                                            departamento = departamento,
                                            ubicacion = ubicacion,
                                            descripcion = descripcion,
                                            observaciones = observaciones,
                                            estado = estado,
                                            responsable = responsable,
                                            fecha_resolucion = fechaResolucion,
                                            tiempo_invertido = tiempoInvertido,
                                            mas_info = null,
                                            comentarios = emptyList(),
                                            incidenciaHardware = null,
                                            incidenciaSoftware = incidenciaSoftware
                                        )

                                    }
                                    if(descripcion == ""||descripcion.length>80||(observaciones?.length ?:0) >400||errorFecha|| ubicacion == null
                                        || tipo == "" || (modelo == "" || numserie == "" || tipoHw == null) && (software == "" || clave == "" || so == "" )){
                                        error = true
                                    }else{
                                        if(tipo == "HW"){
                                            if(incidenciaSoftware!=null){
                                                onIncidenciaSoftwareEliminada(incidenciaSoftware!!)
                                            }
                                        }else{
                                            if(incidenciaHardware!=null){
                                                onIncidenciaHardwareEliminada(incidenciaHardware!!)
                                            }
                                        }
                                        onInsertarPulsado(incidenciaNueva)
                                        Toast.makeText(contexto, "Comentario insertado correctamente", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    var incidenciaActu: Incidencia
                                    if (tipo == "HW") {
                                        incidenciaHardware = IncidenciaHardware(
                                            idh = incidencia.incidenciaHardware?.idh ?: 0,
                                            modelo = modelo,
                                            numSerie = numserie,
                                            tipoHw = tipoHw
                                        )
                                        incidenciaActu = Incidencia(
                                            idIncidencia = incidencia.idIncidencia,
                                            tipo = tipo,
                                            fecha_incidencia = fechaIncidencia,
                                            fecha_introduccion = formatearFecha(getFechaActual()),
                                            profesor = incidencia.profesor,
                                            departamento = departamento,
                                            ubicacion = ubicacion,
                                            descripcion = descripcion,
                                            observaciones = observaciones,
                                            estado = estado,
                                            responsable = responsable,
                                            fecha_resolucion = fechaResolucion,
                                            tiempo_invertido = tiempoInvertido,
                                            mas_info = null,
                                            comentarios = comentarios,
                                            incidenciaHardware = incidenciaHardware,
                                            incidenciaSoftware = null
                                        )

                                    } else {
                                        incidenciaSoftware = IncidenciaSoftware(
                                            ids = incidencia.incidenciaSoftware?.ids ?: 0,
                                            software = software,
                                            clave = clave,
                                            SO = so
                                        )
                                        incidenciaActu = Incidencia(
                                            idIncidencia = incidencia.idIncidencia,
                                            tipo = tipo,
                                            fecha_incidencia = fechaIncidencia,
                                            fecha_introduccion = formatearFecha(getFechaActual()),
                                            profesor = incidencia.profesor,
                                            departamento = departamento,
                                            ubicacion = ubicacion,
                                            descripcion = descripcion,
                                            observaciones = observaciones,
                                            estado = estado,
                                            responsable = responsable,
                                            fecha_resolucion = fechaResolucion,
                                            tiempo_invertido = tiempoInvertido,
                                            mas_info = null,
                                            comentarios = comentarios,
                                            incidenciaHardware = null,
                                            incidenciaSoftware = incidenciaSoftware
                                        )
                                    }
                                    if(descripcion == "" ||descripcion.length>80||(observaciones?.length ?:0) >400||errorFecha|| ubicacion == null
                                        || tipo == "" || (modelo == "" || numserie == "" || tipoHw == null) && (software == "" || clave == "" || so == "" )){
                                        error = true
                                    }else{
                                        if(tipo == "HW"){
                                            if(incidenciaSoftware!=null){
                                                onIncidenciaSoftwareEliminada(incidenciaSoftware!!)
                                            }
                                        }else{
                                            if(incidenciaHardware!=null){
                                                onIncidenciaHardwareEliminada(incidenciaHardware!!)
                                            }
                                        }
                                        onActualizarPulsado(incidenciaActu)
                                        Toast.makeText(contexto, "Comentario insertado correctamente", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Filled.Send,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
    if(mostrarDialogoUbicacionDetalles){
        DialogoUbicacionDetalles(onDismiss = {mostrarDialogoUbicacionDetalles = false}, ubicacion = ubicacion!!)
    }
}
