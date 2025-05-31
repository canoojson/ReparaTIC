package com.example.reparatic.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.reparatic.R
import com.example.reparatic.ui.ViewModels.LoginUIState
import com.example.reparatic.ui.encriptarMD5

@Composable
fun PantallaLogin(
    uiState: LoginUIState,
    onLogin: (String, String) -> Unit,
    onLoginSuccess: () -> Unit,
) {
    var login by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var pwdMD5 by remember { mutableStateOf("") }
    var verContraseña by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally ,verticalArrangement = Arrangement.Center) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally ,verticalArrangement = Arrangement.Center) {
                Image(
                    modifier= Modifier.size(250.dp),
                    imageVector = Icons.Filled.Edit,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                TextField(
                    value = login,
                    onValueChange = { login = it },
                    label = {
                        Text("Login")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(16.dp,16.dp,16.dp,16.dp)
                        .width(200.dp),
                    singleLine = true
                )
                Row(modifier = Modifier.padding(75.dp,16.dp,16.dp,16.dp)) {
                    if(!verContraseña) {
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
                            modifier = Modifier.width(200.dp),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if(pwd==""){
                                        onLogin(login, pwd)
                                    }else{
                                        pwdMD5 = encriptarMD5(pwd)
                                        onLogin(login, pwdMD5)
                                    }
                                }
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
                            value = pwd,
                            onValueChange = { pwd = it },
                            label = {
                                Text("Contraseña")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.width(200.dp),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if(pwd==""){
                                        onLogin(login, pwd)
                                    }else{
                                        pwdMD5 = encriptarMD5(pwd)
                                        onLogin(login, pwdMD5)
                                    }
                                }
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
                                painter = painterResource(R.drawable.invisible),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    }
                }

                when (uiState) {
                    is LoginUIState.ObtenerExito -> {
                        onLoginSuccess()
                    }
                    is LoginUIState.Error -> {
                        Text(uiState.mensaje, color = Color.Red)
                    }
                    is LoginUIState.Cargando -> {

                    }
                }

                Button(onClick = {
                    if(pwd==""){
                        onLogin(login, pwd)
                    }else{
                        pwdMD5 = encriptarMD5(pwd)
                        onLogin(login, pwdMD5)
                    }
                },
                    modifier = Modifier.padding(16.dp,16.dp,16.dp,16.dp),
                )
                {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}
