package com.example.reparatic.ui.pantallas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reparatic.ui.LoginUIState
import com.example.reparatic.ui.Pantallas

@Composable
fun PantallaLogin(
    uiState: LoginUIState,
    onLogin: (String, String) -> Unit,
    navController: NavController
) {
    var login by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally ,verticalArrangement = Arrangement.Center) {
        TextField(
            value = login,
            onValueChange = { login = it },
            label = {
                Text("Login")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
        TextField(
            value = pwd,
            onValueChange = { pwd = it },
            label = {
                Text("Contraseña")
                    },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLogin(login, pwd)
                }
            )
        )

        Button(onClick = {
            onLogin(login, pwd)
        })
        {
            Text("Iniciar sesión")
        }

        when (uiState) {
            is LoginUIState.Error -> Text(uiState.mensaje, color = Color.Red)
            is LoginUIState.ObtenerExito -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Pantallas.Incidencias.name) {
                        popUpTo(0) // evita volver atrás
                    }
                }
            }
            else -> {

            }

        }
    }
}
