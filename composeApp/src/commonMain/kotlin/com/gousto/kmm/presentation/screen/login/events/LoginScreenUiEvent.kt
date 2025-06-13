package com.gousto.kmm.presentation.screen.login.events

sealed class LoginScreenUiEvent {
    data object LoginSuccess : LoginScreenUiEvent()
    data class ShowError(val message: String) : LoginScreenUiEvent()
    data class LoginSuccessAndGameStarted(val sessionId: String) : LoginScreenUiEvent()
}