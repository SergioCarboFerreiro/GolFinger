package com.gousto.kmm.presentation.screen.newRound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.data.remote.firebase.roundRepository.RoundModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.domain.GetAllUsersProfileUseCase
import com.gousto.kmm.domain.SaveRoundUseCase
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
    private val getAllUsersUseCase: GetAllUsersProfileUseCase,
    private val saveRoundUseCase: SaveRoundUseCase
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

    fun onStartRoundClicked() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            val course = _uiState.value.selectedCourse
            val selectedPlayers = _uiState.value.selectedPlayers
            val selectedPlayerModels = _uiState.value.players.filter { it.id in selectedPlayers }

            if (course != null && selectedPlayers.isNotEmpty()) {
                val sessionId = generateSessionId()
                saveRoundSession(sessionId, course, selectedPlayerModels)
                _event.emit(
                    NewRoundScreenUiEvent.RoundCreated(sessionId = sessionId)
                )
            } else {
                _event.emit(NewRoundScreenUiEvent.ShowError("Selecciona un campo y al menos un jugador."))
            }
        }
    }

    private fun saveRoundSession(
        sessionId: String,
        course: Course,
        players: List<UserProfileModel>
    ) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            val roundModel = RoundModel(
                sessionId = sessionId,
                course = course,
                players = players
            )
            saveRoundUseCase.saveRound(
                roundModel
            )
        }
    }

    private fun generateSessionId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..4)
            .map { chars.random() }
            .joinToString("")
    }
}