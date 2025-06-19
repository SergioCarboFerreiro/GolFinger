package com.gousto.kmm.di

import com.gousto.kmm.presentation.screen.dashboard.DashboardScreenDecorator
import com.gousto.kmm.presentation.screen.dashboard.DashboardScreenViewModel
import com.gousto.kmm.presentation.screen.login.LoginScreenDecorator
import com.gousto.kmm.presentation.screen.login.LoginScreenViewModel
import com.gousto.kmm.presentation.screen.newRound.NewRoundScreenDecorator
import com.gousto.kmm.presentation.screen.newRound.NewRoundScreenViewModel
import com.gousto.kmm.presentation.screen.newRound.courses.SelectCourseScreenDecorator
import com.gousto.kmm.presentation.screen.newRound.courses.SelectCourseScreenViewModel
import com.gousto.kmm.presentation.screen.profile.ProfileScreenDecorator
import com.gousto.kmm.presentation.screen.profile.ProfileScreenViewModel
import com.gousto.kmm.presentation.screen.register.RegisterScreenViewModel
import com.gousto.kmm.presentation.screen.round.RoundScreenViewModel
import com.gousto.kmm.presentation.screen.splash.SplashScreenDecorator
import com.gousto.kmm.presentation.screen.splash.SplashScreenViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::RegisterScreenViewModel)
    viewModelOf(::DashboardScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
    viewModelOf(::NewRoundScreenViewModel)
    viewModelOf(::RoundScreenViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::SelectCourseScreenViewModel)

    factoryOf(::LoginScreenDecorator)
    factoryOf(::DashboardScreenDecorator)
    factoryOf(::ProfileScreenDecorator)
    factoryOf(::SplashScreenDecorator)
    factoryOf(::NewRoundScreenDecorator)
    factoryOf(::SelectCourseScreenDecorator)
}