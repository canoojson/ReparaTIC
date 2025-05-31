package com.example.reparatic.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reparatic.R
import com.example.reparatic.datos.DrawerMenu
import com.example.reparatic.modelo.Incidencia
import com.example.reparatic.modelo.Profesor
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
import com.example.reparatic.ui.pantallas.PantallaIncidencia
import com.example.reparatic.ui.pantallas.PantallaInicioIncidencias
import com.example.reparatic.ui.pantallas.PantallaLogin
import com.example.reparatic.ui.pantallas.PantallaPerfil
import com.example.reparatic.ui.pantallas.getFechaActual
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class Pantallas(@StringRes val titulo: Int) {
    //Incidencias
    Incidencias(titulo = R.string.incidencias),
        Incidencia(titulo = R.string.incidencia),
        IncidenciaNueva(titulo = R.string.incidencia_nueva),
    Login(titulo = R.string.login),
    Perfil(titulo = R.string.perfil)
}

val menu = arrayOf(
    DrawerMenu(Icons.Filled.Warning,Pantallas.Incidencias.titulo, Pantallas.Incidencias.name),
    DrawerMenu(Icons.Filled.Person,Pantallas.Perfil.titulo, Pantallas.Perfil.name)
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
            val uiStateIncidenciaHardware = viewModelIncidenciaHardware.estadoUI
            val uiStateIncidenciaSoftware = viewModelIncidenciaSoftware.estadoUI

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
                        },
                        onActualizarPulsado = {
                            Log.v("Cguuigfeihsjgfi", "onActualizarPulsado")
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
                    PantallaPerfil(
                        profesor = viewModelLogin.login,
                        uiStateDepto = uiStateDepartamento,
                        uiStateRol = uiStateRol,
                        uiStateProfesor = uiStateProfesor,
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
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ){
            Image(
                modifier= Modifier.size(250.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Filled.AccountCircle,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Text(
                text = login.nombre + " " + login.apellidos,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        Spacer(modifier = Modifier.size(100.dp))
        menu.forEach {
            NavigationDrawerItem(
                label = {
                    Text(text = stringResource(id = it.titulo))
                },
                icon = { Icon(imageVector = it.icono, contentDescription = null)},
                selected = it.titulo == pantallaActual.titulo,
                onClick = {
                    onMenuClick(it.ruta)
                }
            )
        }
        Row(modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.Bottom) {

            NavigationDrawerItem(
                label = {
                    Text(text = stringResource(id = R.string.cerrar_sesion))
                },
                icon = { Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = null)},
                selected = false,
                onClick = {
                    onCerrarSesion()
                    onMenuClick(Pantallas.Login.name)
                }
            )
        }

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
