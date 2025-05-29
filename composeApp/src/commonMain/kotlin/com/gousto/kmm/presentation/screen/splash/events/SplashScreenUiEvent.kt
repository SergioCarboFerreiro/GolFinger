package com.gousto.kmm.presentation.screen.splash.events

import com.gousto.kmm.presentation.screen.login.events.LoginScreenUiEvent

sealed class SplashScreenUiEvent {
    data object NotLoggedIn : SplashScreenUiEvent()
    data object LoggedInNoRound : SplashScreenUiEvent()
    data class UserInRound(val sessionId: String) : SplashScreenUiEvent()
    data class ShowError(val message: String) : SplashScreenUiEvent()
}