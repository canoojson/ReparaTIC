package com.example.reparatic.ui


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.reparatic.R
import java.text.ParseException
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
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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
        modifier = Modifier.padding(0.dp,32.dp,0.dp,0.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.calendario),
            contentDescription = "Editar ubicación",
            modifier = Modifier.size(20.dp)
        )
    }
}
@SuppressLint("DefaultLocale")
@Composable
fun TimePickerWithFormattedString(
    onDurationSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val duration = String.format("%02d:%02d:00", selectedHour, selectedMinute)
            onDurationSelected(duration)
        },
        hour,
        minute,
        true
    )

    Button(
        onClick = { timePickerDialog.show() },
        colors = ButtonDefaults.buttonColors(
            Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent
        ),
        modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.reloj),
            contentDescription = "Seleccionar tiempo invertido",
            modifier = Modifier.size(20.dp)
        )
    }
}
fun formatearFecha(fechaOriginal: String?): String {
    return try {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fecha = formatoEntrada.parse(fechaOriginal ?: "")
        fecha?.let { formatoSalida.format(it) } ?: "Fecha inválida"
    } catch (e: Exception) {
        "Fecha inválida"
    }
}

fun esFechaValida(fecha: String): Boolean {
    Log.v("Juan", "h")
    return try {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fechaIngresada = formato.parse(fecha)
        val fechaHoy = formato.parse(formatearFecha(getFechaActual()))
        if (fechaIngresada != null) {
            Log.v("Juan", fechaIngresada.toString() )
        }
        // Si fechaIngresada es posterior a hoy, no es válida
        Log.v("Juan", (fechaIngresada != null && !fechaIngresada.after(fechaHoy)).toString())
        fechaIngresada != null && !fechaIngresada.after(fechaHoy)
    } catch (e: ParseException) {
        false
    }
}

fun getFechaActual(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}