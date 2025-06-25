package com.gousto.kmm.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.data.remote.firebase.roundRepository.RoundModel
import com.gousto.kmm.domain.AuthUserUseCase
import com.gousto.kmm.domain.GetActiveRoundSessionIdByUserIdUseCase
import com.gousto.kmm.domain.GetRoundByIdUseCase
import com.gousto.kmm.presentation.screen.login.events.LoginScreenUiEvent
import com.gousto.kmm.presentation.screen.login.state.LoginScreenUiState
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
    private val getRoundByIdUseCase: GetRoundByIdUseCase,
    private val getActiveRoundSessionIdByUserIdUseCase: GetActiveRoundSessionIdByUserIdUseCase
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
            if (!validateLogin()) {
                _event.emit(LoginScreenUiEvent.ShowError("Introduce usuario y contraseña."))
                return@launch
            }

            val state = _uiState.value

            val authResult = authUserUseCase.authUser(
                email = state.username,
                password = state.password
            )

            val user = authResult.user
            if (user == null) {
                _event.emit(LoginScreenUiEvent.ShowError("Usuario o contraseña incorrectos."))
                return@launch
            }

            val sessionId = getActiveRoundSessionIdByUserIdUseCase.get(user.uid)
            val round = sessionId?.let { getRoundByIdUseCase.getRoundById(it) }

            when {
                sessionId != null && round?.isFinished == false -> {
                    _event.emit(LoginScreenUiEvent.LoginSuccessAndGameStarted(sessionId))
                }
                else -> {
                    _event.emit(LoginScreenUiEvent.LoginSuccess)
                }
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

