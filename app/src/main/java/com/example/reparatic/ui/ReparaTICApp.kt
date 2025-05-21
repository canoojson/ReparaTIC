package com.example.reparatic.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.reparatic.modelo.Profesor
import com.example.reparatic.ui.pantallas.PantallaIncidencia
import com.example.reparatic.ui.pantallas.PantallaInicioIncidencias
import com.example.reparatic.ui.pantallas.PantallaLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class Pantallas(@StringRes val titulo: Int) {
    //Incidencias
    Incidencias(titulo = R.string.incidencias),
        Incidencia(titulo = R.string.incidencia),
    Login(titulo = R.string.login)
}

val menu = arrayOf(
    DrawerMenu(Icons.Filled.Home,Pantallas.Incidencias.titulo, Pantallas.Incidencias.name)
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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController()
){
    val pilaRetroceso by navController.currentBackStackEntryAsState()

    val pantallaActual = Pantallas.valueOf(
        pilaRetroceso?.destination?.route ?: Pantallas.Incidencias.name
    )

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
                    ) { ruta ->
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navController.navigate(ruta)
                    }
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

            NavHost(
                navController = navController,
                startDestination = Pantallas.Login.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Pantallas.Login.name) {
                    PantallaLogin(
                        uiState = viewModelLogin.estado,
                        onLogin = { usuario, contrasena -> viewModelLogin.iniciarSesion(usuario, contrasena) },
                        navController = navController
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
                    PantallaIncidencia(
                        incidencia = viewModelIncidecia.incidenciaPulsada,
                        login = viewModelLogin.login,
                        uiStatePro = uiStateProfesor,
                        uiStateDep = uiStateDepartamento,
                        uiStateEst = uiStateEstado,
                        uiStateUbi = uiStateUbicacion,
                        uiStateTiposHw= uiStateTiposHw,
                        onActualizarPulsado = {
                            Log.v("ReparaTICApp", it.responsable.toString())
                            viewModelIncidecia.actualizarIncidencia(it.idIncidencia, it)
                        },
                        onEliminarPulsado = {
                            viewModelIncidecia.eliminarIncidencia(it)
                            navController.navigate(route = Pantallas.Incidencias.name)
                        },
                        onUbicacionActualizada = {
                            viewModelUbicacion.actualizarUbicacion(it.idUbicacion, it)
                            Log.v("ReparaTICApp", it.toString())
                            navController.navigate(route = Pantallas.Incidencias.name)
                        },
                        onUbicacionEliminada = {
                            viewModelUbicacion.eliminarUbicacion(it)
                            navController.navigate(route = Pantallas.Incidencias.name)
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
    onMenuClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ){
            Image(
                modifier= Modifier.size(150.dp)
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
        Spacer(modifier = Modifier.size(150.dp))
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
