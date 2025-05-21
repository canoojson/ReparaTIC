package com.example.reparatic.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun detectarTipoArchivo(bytes: ByteArray?): String {
    if (bytes==null || bytes.size < 4) return "desconocido"

    val header = bytes.take(4).toByteArray()

    return when {
        header[0] == 0xFF.toByte() && header[1] == 0xD8.toByte() -> "imagen/jpeg"
        header[0] == 0x89.toByte() && header[1] == 0x50.toByte() &&
                header[2] == 0x4E.toByte() && header[3] == 0x47.toByte() -> "imagen/png"
        header[0] == 0x25.toByte() && header[1] == 0x50.toByte() &&
                header[2] == 0x44.toByte() && header[3] == 0x46.toByte() -> "application/pdf"
        else -> "desconocido"
    }
}

fun guardarPdfTemporal(context: Context, bytes: ByteArray, nombreArchivo: String = "temp.pdf"): File {
    val file = File(context.cacheDir, nombreArchivo)
    file.outputStream().use { it.write(bytes) }
    return file
}