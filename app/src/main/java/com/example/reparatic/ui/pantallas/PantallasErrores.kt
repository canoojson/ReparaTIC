package com.example.reparatic.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.reparatic.R

@Composable
fun PantallaCargando(modifier: Modifier = Modifier){
    CircularProgressIndicator()
}

@Composable
fun PantallaError(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.error),
        contentDescription = stringResource(id = R.string.error),
        modifier = modifier
    )
}