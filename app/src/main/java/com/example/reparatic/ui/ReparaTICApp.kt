package com.example.reparatic.ui

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.reparatic.R
import com.example.reparatic.datos.DrawerMenu
import com.example.reparatic.modelo.Departamento
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.modelo.Rol
import com.example.reparatic.modelo.Ubicacion
import com.example.reparatic.ui.ViewModels.DepartamentoViewModel
import com.example.reparatic.ui.ViewModels.EstadoViewModel
import com.example.reparatic.ui.ViewModels.IncidenciaHardwareViewModel
import com.example.reparatic.ui.ViewModels.IncidenciaSoftwareViewModel
import com.example.reparatic.ui.ViewModels.IncidenciaViewModel
import com.example.reparatic.ui.ViewModels.LoginViewModel
import com.example.reparatic.ui.ViewModels.ProfesorViewModel
import com.example.reparatic.ui.ViewModels.RolViewModel
import com.example.reparatic.ui.ViewModels.TiposHwViewModel
import com.example.reparatic.ui.ViewModels.UbicacionViewModel
import com.example.reparatic.ui.pantallas.DepartamentoDialog
import com.example.reparatic.ui.pantallas.PantallaIncidencia
import com.example.reparatic.ui.pantallas.PantallaInicioDepartamentos
import com.example.reparatic.ui.pantallas.PantallaInicioIncidencias
import com.example.reparatic.ui.pantallas.PantallaInicioProfesores
import com.example.reparatic.ui.pantallas.PantallaInicioRoles
import com.example.reparatic.ui.pantallas.PantallaInicioUbicaciones
import com.example.reparatic.ui.pantallas.PantallaLogin
import com.example.reparatic.ui.pantallas.PantallaProfesor
import com.example.reparatic.ui.pantallas.PantallaUbicacion
import com.example.reparatic.ui.pantallas.RolDialog
import com.example.reparatic.ui.pantallas.getFechaActual
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class Pantallas(@StringRes val titulo: Int) {
    //Incidencias
    Incidencias(titulo = R.string.incidencias),
        Incidencia(titulo = R.string.incidencia),
        IncidenciaNueva(titulo = R.string.incidencia_nueva),
    Login(titulo = R.string.login),
    Perfil(titulo = R.string.perfil),
    Ubicaciones(titulo = R.string.ubicaciones),
        Ubicacion(titulo = R.string.ubicacion),
        NuevaUbicacion(titulo = R.string.nueva_ubicacion),
    Departamentos(titulo = R.string.departamentos),
        Departamento(titulo= R.string.departamento),
        NuevoDepartamento(titulo = R.string.nuevo_departamento),
    Profesores(titulo = R.string.profesores),
        Profesor(titulo = R.string.profesor),
        NuevoProfesor(titulo = R.string.nuevo_profesor),
    GestionPermisos(titulo = R.string.gestion_permisos),
        NuevoRol(titulo = R.string.nuevorol)
}

val menu = arrayOf(
    DrawerMenu(Icons.Filled.Warning,Pantallas.Incidencias.titulo, Pantallas.Incidencias.name),
    DrawerMenu(Icons.Filled.Person,Pantallas.Perfil.titulo, Pantallas.Perfil.name),
    DrawerMenu(Icons.Filled.Place,Pantallas.Ubicaciones.titulo, Pantallas.Ubicaciones.name),
    DrawerMenu(Icons.Filled.Create,Pantallas.Departamentos.titulo, Pantallas.Departamentos.name),
    DrawerMenu(Icons.Filled.Face,Pantallas.Profesores.titulo, Pantallas.Profesores.name),
    DrawerMenu(Icons.Filled.Lock,Pantallas.GestionPermisos.titulo, Pantallas.GestionPermisos.name)
)


@Composable
fun ReparaTICApp(
    viewModelLogin: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
    viewModelIncidecia: IncidenciaViewModel = viewModel(factory = IncidenciaViewModel.Factory),
    viewModelProfesor: ProfesorViewModel = viewModel(factory = ProfesorViewModel.Factory),
    viewModelDepartamento: DepartamentoViewModel = viewModel(factory = DepartamentoViewModel.Factory),
    viewModelEstado: EstadoViewModel = viewModel(factory = EstadoViewModel.Factory),
    viewModelUbicacion: UbicacionViewModel = viewModel(factory = UbicacionViewModel.Factory),
    viewModelTiposHw: TiposHwViewModel = viewModel(factory = TiposHwViewModel.Factory),
    viewModelRol: RolViewModel = viewModel(factory = RolViewModel.Factory),
    viewModelIncidenciaHardware: IncidenciaHardwareViewModel = viewModel(factory = IncidenciaHardwareViewModel.Factory),
    viewModelIncidenciaSoftware: IncidenciaSoftwareViewModel = viewModel(factory = IncidenciaSoftwareViewModel.Factory),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController()
){
    val pilaRetroceso by navController.currentBackStackEntryAsState()

    val pantallaActual = Pantallas.valueOf(
        pilaRetroceso?.destination?.route ?: Pantallas.Incidencias.name
    )
    fun onCerrarSesion(){
        viewModelLogin.cerrarSesion()
        navController.navigate(Pantallas.Login.name){
            popUpTo(0)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = pantallaActual != Pantallas.Login,
        drawerContent = {
            ModalDrawerSheet {
                if(pantallaActual != Pantallas.Login) {
                    DrawerContent(
                        menu = menu,
                        pantallaActual = pantallaActual,
                        login = viewModelLogin.login,
                        onMenuClick = { ruta ->
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navController.navigate(ruta)
                        },
                        onCerrarSesion = {
                            onCerrarSesion()
                        }
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    pantallaActual = pantallaActual,
                    drawerState = drawerState
                )
            },
            floatingActionButton = {
                if(pantallaActual == Pantallas.Incidencias){
                    FloatingActionButton(onClick = { navController.navigate(route = Pantallas.IncidenciaNueva.name) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Nuevo")
                    }
                }

                if(pantallaActual == Pantallas.Ubicaciones){
                    FloatingActionButton(onClick = { navController.navigate(route = Pantallas.NuevaUbicacion.name) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Nuevo")
                    }
                }

                if(pantallaActual == Pantallas.Profesores){
                    FloatingActionButton(onClick = { navController.navigate(route = Pantallas.NuevoProfesor.name) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Nuevo")
                    }
                }
                if(pantallaActual == Pantallas.Departamentos){
                    FloatingActionButton(onClick = { navController.navigate(route = Pantallas.NuevoDepartamento.name) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Nuevo")
                    }
                }

                if(pantallaActual == Pantallas.GestionPermisos){
                    FloatingActionButton(onClick = { navController.navigate(route = Pantallas.NuevoRol.name) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Nuevo")
                    }
                }
            }
        ) {
            innerPadding ->

            val uiStateIncidencia = viewModelIncidecia.incidenciaUIState
            val uiStateLogin = viewModelLogin.estado
            val uiStateProfesor = viewModelProfesor.profesorUIState
            val uiStateDepartamento = viewModelDepartamento.departamentoUIState
            val uiStateEstado = viewModelEstado.estadoUIState
            val uiStateUbicacion by rememberUpdatedState(viewModelUbicacion.ubicacionUIState)
            val uiStateTiposHw by rememberUpdatedState(viewModelTiposHw.tiposHwUIState)
            val uiStateRol by rememberUpdatedState(viewModelRol.rolUIState)

            NavHost(
                navController = navController,
                startDestination = Pantallas.Login.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Pantallas.Login.name) {
                    PantallaLogin(
                        uiState = uiStateLogin,
                        onLogin = { usuario, contrasena -> viewModelLogin.iniciarSesion(usuario, contrasena)},
                        onLoginSuccess = {
                            navController.navigate(Pantallas.Incidencias.name){
                                popUpTo(0)
                            }
                        }
                    )
                }
                composable(route = Pantallas.Incidencias.name) {
                    PantallaInicioIncidencias(
                        appUIState = uiStateIncidencia,
                        login = viewModelLogin.login,
                        onIncidenciasObtenidas = {viewModelIncidecia.obtenerIncidencias()},
                        onIncidenciaPulsada = {
                            viewModelIncidecia.actualizarIncidenciaPulsada(it)
                            navController.navigate(route = Pantallas.Incidencia.name)
                        }
                    )
                }
                composable(route= Pantallas.Incidencia.name){

                    LaunchedEffect(Unit) {
                        viewModelProfesor.obtenerProfesoresDepartamento("Informática")
                    }
                    LaunchedEffect(Unit) {
                        viewModelUbicacion.obtenerUbicaciones()
                    }

                    PantallaIncidencia(
                        incidencia = viewModelIncidecia.incidenciaPulsada,
                        login = viewModelLogin.login,
                        uiStatePro = uiStateProfesor,
                        uiStateDep = uiStateDepartamento,
                        uiStateEst = uiStateEstado,
                        uiStateUbi = uiStateUbicacion,
                        uiStateTiposHw= uiStateTiposHw,
                        onInsertarPulsado = {
                            viewModelIncidecia.insertarIncidencia(it)
                            navController.navigate(route = Pantallas.Incidencias.name)
                        },
                        onActualizarPulsado = {
                            viewModelIncidecia.actualizarIncidencia(it.idIncidencia, it)
                        },
                        onEliminarPulsado = {
                            viewModelIncidecia.eliminarIncidencia(it)
                            navController.navigate(route = Pantallas.Incidencias.name)
                        },
                        onUbicacionActualizada = {
                            viewModelUbicacion.actualizarUbicacion(it.idUbicacion, it)
                        },
                        onUbicacionEliminada = {
                            viewModelUbicacion.eliminarUbicacion(it)
                        },
                        onIncidenciaHardwareEliminada = {
                            Log.v("Cguuigfeihsjgfi", "onIncidenciaSoftwareEliminada")
                            viewModelIncidenciaHardware.eliminarIncidenciaHardware(it.idh)
                        },
                        onIncidenciaSoftwareEliminada = {
                            Log.v("Cguuigfeihsjgfi", "onIncidenciaSoftwareEliminada")
                            viewModelIncidenciaSoftware.eliminar(it.ids)
                        }
                    )
                }
                composable(route= Pantallas.IncidenciaNueva.name){
                    LaunchedEffect(Unit) {
                        viewModelProfesor.obtenerProfesoresDepartamento("Informática")
                    }
                    LaunchedEffect(Unit) {
                        viewModelUbicacion.obtenerUbicaciones()
                    }
                    PantallaIncidencia(
                        incidencia = Incidencia(
                            idIncidencia = 0, tipo = "", fecha_incidencia = "", profesor = null,
                            departamento = null, ubicacion = null, descripcion = "", fecha_introduccion = getFechaActual(),
                            comentarios = emptyList(), fecha_resolucion = "", tiempo_invertido = "00:00:00", responsable = null, estado = null, mas_info = null, observaciones = "",
                            incidenciaHardware = null, incidenciaSoftware = null),
                        login = viewModelLogin.login,
                        uiStatePro = uiStateProfesor,
                        uiStateDep = uiStateDepartamento,
                        uiStateEst = uiStateEstado,
                        uiStateUbi = uiStateUbicacion,
                        uiStateTiposHw= uiStateTiposHw,
                        onInsertarPulsado = {
                            viewModelIncidecia.insertarIncidencia(it)
                            navController.navigate(route = Pantallas.Incidencias.name)
                        },
                        onActualizarPulsado = {
                            viewModelIncidecia.actualizarIncidencia(it.idIncidencia, it)
                        },
                        onEliminarPulsado = {
                            viewModelIncidecia.eliminarIncidencia(it)
                            navController.navigate(route = Pantallas.Incidencias.name)
                        },
                        onUbicacionActualizada = {
                            viewModelUbicacion.actualizarUbicacion(it.idUbicacion, it)
                        },
                        onUbicacionEliminada = {
                            viewModelUbicacion.eliminarUbicacion(it)
                        },
                        onIncidenciaHardwareEliminada = {
                            viewModelIncidenciaHardware.eliminarIncidenciaHardware(it.idh)
                        },
                        onIncidenciaSoftwareEliminada = {
                            viewModelIncidenciaSoftware.eliminar(it.ids)
                        }
                    )
                }
                composable(route = Pantallas.Perfil.name){
                    PantallaProfesor(
                        profesor = viewModelLogin.login,
                        login = viewModelLogin.login,
                        uiStateDepto = uiStateDepartamento,
                        uiStateRol = uiStateRol,
                        uiStateProfesor = uiStateProfesor,
                        onInsertarPulsado = {
                            viewModelProfesor.insertarProfesor(it)
                            navController.navigate(route = Pantallas.Profesores.name)
                        },
                        onActualizarPulsado = {
                            viewModelProfesor.actualizarProfesor(it.idProfesor, it)
                        },
                        onEliminarPulsado = {
                            viewModelProfesor.eliminarProfesor(it.idProfesor)
                            onCerrarSesion()
                            navController.navigate(route = Pantallas.Login.name)
                        }

                    )
                }
                composable(route = Pantallas.Ubicaciones.name) {
                    PantallaInicioUbicaciones(
                        appUIState = uiStateUbicacion,
                        onUbicacionesObtenidas = {viewModelUbicacion.obtenerUbicaciones()},
                        onUbicacionPulsada = {
                            viewModelUbicacion.actualizarUbicacionPulsada(it)
                            navController.navigate(route = Pantallas.Ubicacion.name)
                        }
                    )
                }
                composable(route= Pantallas.Ubicacion.name) {
                    PantallaUbicacion(
                        ubicacion = viewModelUbicacion.ubicacionPulsada,
                        onUbicacionInsertada = {
                            viewModelUbicacion.insertarUbicacion(it)
                            navController.navigate(route = Pantallas.Ubicaciones.name)
                        },
                        onUbicacionActualizada = {
                            viewModelUbicacion.actualizarUbicacion(it.idUbicacion, it)
                        },
                        onUbicacionEliminada = {
                            viewModelUbicacion.eliminarUbicacion(it)
                            navController.navigate(route = Pantallas.Ubicaciones.name)
                        }
                    )
                }
                composable(route= Pantallas.NuevaUbicacion.name) {
                    PantallaUbicacion(
                        ubicacion = Ubicacion(idUbicacion = 0, nombre = "", descrip = ""),
                        onUbicacionInsertada = {
                            viewModelUbicacion.insertarUbicacion(it)
                            navController.navigate(route = Pantallas.Ubicaciones.name)
                        },
                        onUbicacionActualizada = {
                            viewModelUbicacion.actualizarUbicacion(it.idUbicacion, it)
                        },
                        onUbicacionEliminada = {
                            viewModelUbicacion.eliminarUbicacion(it)
                            navController.navigate(route = Pantallas.Ubicaciones.name)
                        }
                    )
                }
                composable(route= Pantallas.Departamentos.name) {
                    PantallaInicioDepartamentos(
                        appUIState = uiStateDepartamento,
                        onDepartamentosObtenidos = {
                            viewModelDepartamento.obtenerDepartamentos()
                        },
                        onDepartamentoPulsado = {
                            viewModelDepartamento.actualizarDepartamentoPulsado(it)
                            navController.navigate(route = Pantallas.Departamento.name)
                        }
                    )
                }
                composable(route= Pantallas.Departamento.name) {
                    DepartamentoDialog(
                        onDismiss = { navController.navigate(route = Pantallas.Departamentos.name) },
                        onActualizarPulsado = {
                            viewModelDepartamento.actualizarDepartamento(it.codDpto, it)
                            navController.navigate(route = Pantallas.Departamentos.name)
                        },
                        onEliminarPulsado = {
                            viewModelDepartamento.eliminarDepartamento(it.codDpto)
                            navController.navigate(route = Pantallas.Departamentos.name)
                        },
                        onInsertarPulsado = {
                            viewModelDepartamento.insertarDepartamento(it)
                            navController.navigate(route = Pantallas.Departamentos.name)
                        },
                        departamento = viewModelDepartamento.departamentoPulsado
                    )
                }
                composable(route= Pantallas.NuevoDepartamento.name) {
                    DepartamentoDialog(
                        onDismiss = { navController.navigate(route = Pantallas.Departamentos.name) },
                        onActualizarPulsado = {
                            viewModelDepartamento.actualizarDepartamento(it.codDpto, it)
                            navController.navigate(route = Pantallas.Departamentos.name)
                        },
                        onEliminarPulsado = {
                            viewModelDepartamento.eliminarDepartamento(it.codDpto)
                            navController.navigate(route = Pantallas.Departamentos.name)
                        },
                        onInsertarPulsado = {
                            viewModelDepartamento.insertarDepartamento(it)
                            navController.navigate(route = Pantallas.Departamentos.name)
                        },
                        departamento = Departamento(codDpto = 0, nombreDpto = "")
                    )
                }
                composable(route= Pantallas.Profesores.name) {
                    PantallaInicioProfesores(
                        appUIState = uiStateProfesor,
                        onProfesoresObtenidos = {
                            viewModelProfesor.obtenerProfesores()
                        },
                        onProfesorPulsado = {
                            viewModelProfesor.actualizarProfesorPulsado(it)
                            navController.navigate(route = Pantallas.Profesor.name)
                        }
                    )
                }
                composable(route= Pantallas.Profesor.name) {
                    PantallaProfesor(
                        profesor = viewModelProfesor.profesorPulsado,
                        login = viewModelLogin.login,
                        uiStateDepto = uiStateDepartamento,
                        uiStateRol = uiStateRol,
                        uiStateProfesor = uiStateProfesor,
                        onInsertarPulsado = {
                            viewModelProfesor.insertarProfesor(it)
                            navController.navigate(route = Pantallas.Profesores.name)
                        },
                        onActualizarPulsado = {
                            viewModelProfesor.actualizarProfesor(it.idProfesor, it)
                        },
                        onEliminarPulsado = {
                            viewModelProfesor.eliminarProfesor(it.idProfesor)
                            navController.navigate(route = Pantallas.Profesores.name)
                        }
                    )
                }
                composable(route= Pantallas.NuevoProfesor.name) {
                    PantallaProfesor(
                        profesor = Profesor(idProfesor = 0, pwd = "", dni = "", username = "", email = "", rol = null, departamento = null, apellidos = "", nombre = ""),
                        uiStateDepto = uiStateDepartamento,
                        login = viewModelLogin.login,
                        uiStateRol = uiStateRol,
                        uiStateProfesor = uiStateProfesor,
                        onInsertarPulsado = {
                            viewModelProfesor.insertarProfesor(it)
                            navController.navigate(route = Pantallas.Profesores.name)
                        },
                        onActualizarPulsado = {
                            viewModelProfesor.actualizarProfesor(it.idProfesor, it)
                        },
                        onEliminarPulsado = {
                            viewModelProfesor.eliminarProfesor(it.idProfesor)
                            navController.navigate(route = Pantallas.Profesores.name)
                        }
                    )
                }
                composable(route= Pantallas.GestionPermisos.name) {
                    PantallaInicioRoles(
                        uiState = uiStateRol,
                        onRolesObtenidos = {
                            viewModelRol.obtenerRoles()
                        },
                        onRolSeleccionado = {
                            viewModelRol.actualizarRolPulsado(it)
                        },
                        onRolEliminado = {
                            viewModelRol.eliminarRol(it.idRol)
                        },
                        rolSeleccionado = viewModelRol.rolPulsado,
                        onActualizarRol = {
                            viewModelRol.actualizarRol(it.idRol, it)
                        }
                    )
                }
                composable(route= Pantallas.NuevoRol.name) {
                    RolDialog(
                        onDismiss = { navController.navigate(route = Pantallas.GestionPermisos.name) },
                        onInsertarPulsado = {
                            viewModelRol.insertarRol(it)
                            navController.navigate(route = Pantallas.GestionPermisos.name)
                        },
                        rol = Rol(idRol = 0, descrip = "", permisos = emptyList())
                    )

                }
            }
        }
    }

}

@Composable
private fun DrawerContent(
    menu: Array<DrawerMenu>,
    pantallaActual: Pantallas,
    login: Profesor,
    onMenuClick: (String) -> Unit,
    onCerrarSesion: () -> Unit,
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Filled.AccountCircle,
                contentScale = ContentScale.Crop,
                contentDescription = "Foto de perfil"
            )
            Text(
                text = "${login.nombre} ${login.apellidos}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.size(50.dp))

        menu.forEach {
            NavigationDrawerItem(
                label = {
                    Text(text = stringResource(id = it.titulo))
                },
                icon = {
                    Icon(imageVector = it.icono, contentDescription = null)
                },
                selected = it.titulo == pantallaActual.titulo,
                onClick = {
                    onMenuClick(it.ruta)
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        NavigationDrawerItem(
            label = {
                Text(text = stringResource(id = R.string.cerrar_sesion))
            },
            icon = {
                Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = null)
            },
            selected = false,
            onClick = {
                onCerrarSesion()
                onMenuClick(Pantallas.Login.name)
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    pantallaActual: Pantallas,
    drawerState: DrawerState?,
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(text = stringResource(id = pantallaActual.titulo)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if(pantallaActual != Pantallas.Login) {
                if (drawerState != null) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.atras)
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}
