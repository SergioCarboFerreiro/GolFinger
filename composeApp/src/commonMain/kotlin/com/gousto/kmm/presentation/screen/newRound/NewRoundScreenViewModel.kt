package com.gousto.kmm.presentation.screen.newRound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.domain.GetAllUsersProfileUseCase
import com.gousto.kmm.presentation.screen.newRound.events.NewRoundScreenUiEvent
import com.gousto.kmm.presentation.screen.newRound.uiState.Course
import com.gousto.kmm.presentation.screen.newRound.uiState.NewRoundScreenUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewRoundScreenViewModel(
    private val getAllUsersUseCase: GetAllUsersProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRoundScreenUiState())
    val uiState: StateFlow<NewRoundScreenUiState> = _uiState

    private val _event = MutableSharedFlow<NewRoundScreenUiEvent>()
    val event = _event.asSharedFlow()

    init {
        fetchPlayers()
    }

    fun selectCourse(course: Course) {
        _uiState.update { it.copy(selectedCourse = course) }
    }

    private fun fetchPlayers() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            _uiState.update { it.copy(isLoading = true) }
            val players = getAllUsersUseCase()
            _uiState.update {
                it.copy(players = players, isLoading = false)
            }

        }
    }

    fun togglePlayerSelection(playerId: String) {
        _uiState.update { state ->
            val updated = if (state.selectedPlayers.contains(playerId)) {
                state.selectedPlayers - playerId
            } else {
                state.selectedPlayers + playerId
            }
            state.copy(selectedPlayers = updated)
        }
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(NewRoundScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}