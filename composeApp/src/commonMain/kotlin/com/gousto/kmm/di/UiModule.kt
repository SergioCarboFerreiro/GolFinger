package com.gousto.kmm.di

import com.gousto.kmm.presentation.screen.login.LoginScreenViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::LoginScreenViewModel)
}