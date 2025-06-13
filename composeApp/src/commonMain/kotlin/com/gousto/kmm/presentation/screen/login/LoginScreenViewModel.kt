package com.gousto.kmm.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.AuthUserUseCase
import com.gousto.kmm.domain.GetActiveCoursesByUserIdUseCase
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.domain.GetRoundByIdUseCase
import com.gousto.kmm.presentation.screen.login.events.LoginScreenUiEvent
import com.gousto.kmm.presentation.screen.login.state.LoginScreenUiState
import com.gousto.kmm.presentation.screen.register.events.RegisterScreenUiEvent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val loginDecorator: LoginScreenDecorator,
    private val authUserUseCase: AuthUserUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val getActiveCoursesByUserIdUseCase: GetActiveCoursesByUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState: StateFlow<LoginScreenUiState> = _uiState

    private val _event = MutableSharedFlow<LoginScreenUiEvent>()
    val event = _event.asSharedFlow()

    init {
        loadInitialState()
    }

    fun onLoginClicked() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            if (validateLogin()) {
                val state = _uiState.value

                val authResult = authUserUseCase.authUser(
                    email = state.username,
                    password = state.password
                )
                val userId = authResult.user?.uid ?: ""
                val sessionId =
                    getActiveCoursesByUserIdUseCase.get(
                        userId
                    )

                if (authResult.user != null && sessionId != null) {
                    _event.emit(LoginScreenUiEvent.LoginSuccessAndGameStarted(sessionId))
                } else if (authResult.user != null) {
                    _event.emit(LoginScreenUiEvent.LoginSuccess)
                } else {
                    _event.emit(LoginScreenUiEvent.ShowError("Usuario o contraseña incorrectos."))
                }

            } else {
                _event.emit(LoginScreenUiEvent.ShowError("Introduce usuario y contraseña."))
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

    private suspend fun handleError(error: Throwable) {
        _event.emit(LoginScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}

