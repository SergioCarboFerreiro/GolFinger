package com.gousto.kmm.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.presentation.screen.splash.events.SplashScreenUiEvent
import com.gousto.kmm.presentation.screen.splash.state.SplashScreenUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val splashScreenDecorator: SplashScreenDecorator
) : ViewModel() {

    private val _event = MutableSharedFlow<SplashScreenUiEvent>()
    val event = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(SplashScreenUiState())
    val uiState: StateFlow<SplashScreenUiState> = _uiState


    fun checkLoginAndActiveRound() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = splashScreenDecorator.getSplashScreenUiState()
            _uiState.value = result
        }
    }


    private suspend fun handleError(error: Throwable) {
        _event.emit(SplashScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}



