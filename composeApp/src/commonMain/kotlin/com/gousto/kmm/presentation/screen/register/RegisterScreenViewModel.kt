package com.gousto.kmm.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.RegisterUserUseCase
import com.gousto.kmm.presentation.screen.login.events.LoginScreenUiEvent
import com.gousto.kmm.presentation.screen.register.state.RegisterScreenUiState

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterScreenViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterScreenUiState())
    val uiState: StateFlow<RegisterScreenUiState> = _uiState
//    •	🔄 El ViewModel vive mientras la pantalla esté en el back stack.
//    •	🧹 Cuando navegas con popUpTo(..., inclusive = true), el ViewModel se destruye.
//    •	📡 El SharedFlow muere con el ViewModel: no conserva los eventos tras destruirse.
    private val _event = MutableSharedFlow<LoginScreenUiEvent>()
    val event = _event.asSharedFlow()

    fun onRegisterClicked() {
        val state = _uiState.value

        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch {
                    _event.emit(LoginScreenUiEvent.ShowError(error.message ?: "Error inesperado."))
                }
            }
        ) {
            if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
                _event.emit(LoginScreenUiEvent.ShowError("Todos los campos excepto el handicap son obligatorios."))
                return@launch
            }

            registerUserUseCase(
                name = state.name,
                email = state.email,
                password = state.password,
                handicap = state.handicap
            )

            _event.emit(LoginScreenUiEvent.LoginSuccess)
        }
    }

    fun onNameChanged(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onHandicapChanged(value: String) {
        _uiState.update { it.copy(handicap = value) }
    }
}