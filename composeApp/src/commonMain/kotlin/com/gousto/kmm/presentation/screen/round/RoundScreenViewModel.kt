package com.gousto.kmm.presentation.screen.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.data.remote.firebase.roundRepository.RoundModel
import com.gousto.kmm.data.remote.firebase.roundRepository.ScoreEntry
import com.gousto.kmm.domain.SaveRoundUseCase
import com.gousto.kmm.presentation.screen.round.events.RoundScreenUiEvent
import com.gousto.kmm.presentation.screen.round.state.RoundScreenUiState
import com.gousto.kmm.location.GeoPoint
import com.gousto.kmm.location.LocationService
import com.gousto.kmm.location.haversine
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoundScreenViewModel(
    private val roundScreenDecorator: RoundScreenDecorator,
    private val saveRoundUseCase: SaveRoundUseCase,
    private val locationService: LocationService
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
            roundScreenDecorator.getRoundById(sessionId)?.let { round ->
                _uiState.value = round.copy(isLoading = false)
            }
        }
    }

    fun updateScore(holeNumber: Int, playerId: String, strokes: String) {
        _uiState.update { current ->
            val updatedScores = current.scores.toMutableMap()
            updatedScores[holeNumber to playerId] = strokes
            current.copy(scores = updatedScores)
        }
    }

    fun measureShot() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            val location = locationService.getLocation()
            val current = _uiState.value
            if (current.startLocation == null) {
                _uiState.update { it.copy(startLocation = location, endLocation = null, shotDistanceMeters = null) }
            } else {
                val distance = haversine(current.startLocation, location)
                _uiState.update { it.copy(endLocation = location, shotDistanceMeters = distance) }
            }
        }
    }

    fun finishRound() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            val state = _uiState.value

            val round = RoundModel(
                sessionId = state.sessionId,
                course = state.course ?: return@launch,
                players = state.players,
                isFinished = true,
                scores = mapScoresToEntries(state.scores)
            )

            saveRoundUseCase.saveRound(round)

            _event.emit(RoundScreenUiEvent.RoundFinished)
        }
    }

    private fun mapScoresToEntries(scores: Map<Pair<Int, String>, String>): List<ScoreEntry> =
        scores.map { (key, value) ->
            ScoreEntry(
                hole = key.first,
                playerId = key.second,
                strokes = value
            )
        }

    private suspend fun handleError(error: Throwable) {
        _event.emit(RoundScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}
