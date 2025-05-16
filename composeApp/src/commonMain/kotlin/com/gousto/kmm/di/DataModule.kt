package com.gousto.kmm.di

import com.gousto.kmm.data.remote.firebase.authRepository.AuthRepository
import com.gousto.kmm.data.remote.firebase.authRepository.AuthRepositoryImpl
import com.gousto.kmm.data.remote.firebase.userRepository.UserRepository
import com.gousto.kmm.data.remote.firebase.userRepository.UserRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::UserRepositoryImpl) bind UserRepository::class
    factoryOf(::AuthRepositoryImpl) bind AuthRepository::class
}