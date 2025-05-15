package com.gousto.kmm.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.presentation.screen.login.events.LoginScreenUiEvent
import com.gousto.kmm.presentation.screen.login.state.LoginScreenUiState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val loginDecorator: LoginScreenDecorator
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState: StateFlow<LoginScreenUiState> = _uiState

    private val _LoginScreen_uiEvent = MutableSharedFlow<LoginScreenUiEvent>()
    val event = _LoginScreen_uiEvent.asSharedFlow()

    init {
        loadInitialState()
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            if (validateLogin()) {
                val state = _uiState.value
                try {
                    val authResult = Firebase.auth.signInWithEmailAndPassword(
                        email = state.username,
                        password = state.password
                    )

                    if (authResult.user != null) {
                        _LoginScreen_uiEvent.emit(LoginScreenUiEvent.LoginSuccess)
                    } else {
                        _LoginScreen_uiEvent.emit(LoginScreenUiEvent.ShowError("Usuario o contraseña incorrectos."))
                    }
                } catch (e: Exception) {
                    _LoginScreen_uiEvent.emit(
                        LoginScreenUiEvent.ShowError(
                            e.message ?: "Ha ocurrido un error inesperado."
                        )
                    )
                }
            } else {
                _LoginScreen_uiEvent.emit(LoginScreenUiEvent.ShowError("Introduce usuario y contraseña."))
            }
        }
    }

    private fun loadInitialState() {
        val loginUiState = loginDecorator.getInitialState()
        _uiState.value = loginUiState
    }

    fun onUsernameChanged(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    private fun validateLogin(): Boolean {
        val current = _uiState.value
        return current.username.isNotBlank() && current.password.isNotBlank()
    }
}

