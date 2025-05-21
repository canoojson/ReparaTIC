package com.example.reparatic

import android.app.Application
import com.example.reparatic.datos.ContenedorApp
import com.example.reparatic.datos.IncidenciaContenedorApp

class ReparaTICAplicacion: Application()  {
    lateinit var contenedor: ContenedorApp
    override fun onCreate() {
        super.onCreate()
        contenedor = IncidenciaContenedorApp(this)
    }
}