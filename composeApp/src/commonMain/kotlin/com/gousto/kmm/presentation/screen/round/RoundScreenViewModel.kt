package com.gousto.kmm.presentation.screen.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.GetRoundByIdUseCase
import com.gousto.kmm.presentation.screen.round.events.RoundScreenUiEvent
import com.gousto.kmm.presentation.screen.round.state.RoundScreenUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoundScreenViewModel(
    private val getRoundByIdUseCase: GetRoundByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoundScreenUiState())
    val uiState: StateFlow<RoundScreenUiState> = _uiState

    private val _event = MutableSharedFlow<RoundScreenUiEvent>()
    val event = _event.asSharedFlow()

    fun loadRound(sessionId: String) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            _uiState.update { it.copy(isLoading = true) }
            val round = getRoundByIdUseCase.getRoundById(sessionId)
            round.let {
                _uiState.value = RoundScreenUiState(
                    sessionId = it?.sessionId ?: "",
                    course = it?.course,
                    players = it?.players ?: emptyList(),
                    isLoading = false
                )
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(RoundScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}