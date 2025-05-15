package com.gousto.kmm.di

import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.domain.RegisterUserUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetCurrentUserProfileUseCase)
    factoryOf(::RegisterUserUseCase)
}