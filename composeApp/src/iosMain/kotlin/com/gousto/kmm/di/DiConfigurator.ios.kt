package com.gousto.kmm.di

import com.gousto.kmm.location.LocationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single { LocationService() }
    }
}
