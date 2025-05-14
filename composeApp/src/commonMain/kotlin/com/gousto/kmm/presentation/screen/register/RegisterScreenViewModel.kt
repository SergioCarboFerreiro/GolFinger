package com.gousto.kmm.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.presentation.screen.login.events.UiEvent
import com.gousto.kmm.presentation.screen.register.state.RegisterScreenUiState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterScreenUiState())
    val uiState: StateFlow<RegisterScreenUiState> = _uiState
//    •	🔄 El ViewModel vive mientras la pantalla esté en el back stack.
//    •	🧹 Cuando navegas con popUpTo(..., inclusive = true), el ViewModel se destruye.
//    •	📡 El SharedFlow muere con el ViewModel: no conserva los eventos tras destruirse.
    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

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

    fun onRegisterClicked() {
        val state = _uiState.value
        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            viewModelScope.launch {
                _event.emit(UiEvent.ShowError("Todos los campos excepto el handicap son obligatorios."))
            }
            return
        }

        viewModelScope.launch {
            try {
                val authResult = Firebase.auth.createUserWithEmailAndPassword(state.email, state.password)
                authResult.user?.let { user ->
                    Firebase.firestore.collection("users").document(user.uid).set(
                        mapOf(
                            "name" to state.name,
                            "handicap" to state.handicap
                        )
                    )
                    _event.emit(UiEvent.LoginSuccess)
                } ?: _event.emit(UiEvent.ShowError("No se pudo crear la cuenta."))
            } catch (e: Exception) {
                _event.emit(UiEvent.ShowError(e.message ?: "Error al registrar."))
            }
        }
    }
}