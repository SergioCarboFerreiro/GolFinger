package com.gousto.kmm.presentation.screen.login

import com.gousto.kmm.presentation.screen.login.state.LoginScreenUiState

class LoginScreenDecorator {
    fun getInitialState(): LoginScreenUiState {
        return LoginScreenUiState(username = "", password = "")
    }
}