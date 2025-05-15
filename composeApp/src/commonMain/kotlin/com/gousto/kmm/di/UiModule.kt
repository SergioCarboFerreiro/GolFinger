package com.gousto.kmm.di

import com.gousto.kmm.presentation.screen.dashboard.DashboardScreenViewModel
import com.gousto.kmm.presentation.screen.login.LoginScreenDecorator
import com.gousto.kmm.presentation.screen.login.LoginScreenViewModel
import com.gousto.kmm.presentation.screen.newRound.NewRoundScreenViewModel
import com.gousto.kmm.presentation.screen.profile.ProfileScreenViewModel
import com.gousto.kmm.presentation.screen.register.RegisterScreenViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::RegisterScreenViewModel)
    viewModelOf(::DashboardScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
    viewModelOf(::NewRoundScreenViewModel)

    factoryOf(::LoginScreenDecorator)
}