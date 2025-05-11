package com.gousto.kmm.presentation.screen.login.state

data class LoginScreenUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false // solo si no usas eventos separados
)