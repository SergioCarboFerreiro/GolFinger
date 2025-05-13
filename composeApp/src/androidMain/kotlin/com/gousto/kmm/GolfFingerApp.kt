package com.gousto.kmm

import android.app.Application
import com.gousto.kmm.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GolfFingerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin {
            androidLogger()
            androidContext(this@GolfFingerApp)
        }
    }
//    Guardar el contexto de GolfFingerApp como instance: es seguro porque:
//    •	Es el contexto de la Application, que vive todo el ciclo de vida de la app.
//    •	No causa memory leaks.
//    •	Es exactamente lo que Firebase, Koin, WorkManager, etc. necesitan.

    companion object {
        lateinit var instance: GolfFingerApp
            private set
    }
}