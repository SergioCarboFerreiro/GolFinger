package com.gousto.kmm.di

import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetCurrentUserProfileUseCase)
}