package com.example.reparatic.ui.pantallas


import android.app.DatePickerDialog
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.reparatic.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithFormattedString(
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = formatter.format(selectedCalendar.time)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    Button(onClick = {
        datePickerDialog.show()
        },
        colors = ButtonDefaults.buttonColors(
            Color.Transparent, Color.Transparent, Color.Transparent,
            Color.Transparent),
        modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.calendario),
            contentDescription = "Editar ubicación",
            modifier = Modifier.size(20.dp)
        )
    }
}
fun formatearFecha(fechaOriginal: String?): String {
    return try {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()) // ajusta según tu formato real
        val formatoSalida = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val fecha = formatoEntrada.parse(fechaOriginal ?: "")
        fecha?.let { formatoSalida.format(it) } ?: "Fecha inválida"
    } catch (e: Exception) {
        "Fecha inválida"
    }
}

fun getFechaActual(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}