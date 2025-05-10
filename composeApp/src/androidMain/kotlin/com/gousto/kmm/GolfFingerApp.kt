package com.gousto.kmm

import android.app.Application
import com.gousto.kmm.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GolfFingerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@GolfFingerApp)
        }
    }
}