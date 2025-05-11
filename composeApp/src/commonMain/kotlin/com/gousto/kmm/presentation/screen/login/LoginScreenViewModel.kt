package com.gousto.kmm.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.presentation.screen.login.events.UiEvent
import com.gousto.kmm.presentation.screen.login.state.LoginScreenUiState
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

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val event = _uiEvent.asSharedFlow()

    init {
        loadInitialState()
    }

    fun onLoginClicked() {

        viewModelScope.launch {
            if (validateLogin()) {
                _uiEvent.emit(UiEvent.LoginSuccess)
            } else {
                _uiEvent.emit(UiEvent.ShowError("Introduce usuario y contraseña"))
            }
        }
    }

    private fun loadInitialState() {
        val stateFromDecorator = loginDecorator.getInitialState()
        _uiState.value = stateFromDecorator
    }

    fun onUsernameChanged(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun validateLogin(): Boolean {
        val current = _uiState.value
        return current.username.isNotBlank() && current.password.isNotBlank()
    }
}

