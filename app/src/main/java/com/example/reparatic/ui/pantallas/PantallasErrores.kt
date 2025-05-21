package com.example.reparatic.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.reparatic.R

@Composable
fun PantallaCargando(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.cargando),
        contentDescription = stringResource(id = R.string.cargando),
        modifier = modifier
    )
}

@Composable
fun PantallaError(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.error),
        contentDescription = stringResource(id = R.string.error),
        modifier = modifier
    )
}