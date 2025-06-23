package com.gousto.kmm.presentation.screen.newRound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.data.remote.firebase.courseRepository.CourseModel
import com.gousto.kmm.data.remote.firebase.courseRepository.GameModeModel
import com.gousto.kmm.data.remote.firebase.courseRepository.HoleModel
import com.gousto.kmm.data.remote.firebase.roundRepository.RoundModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.domain.SaveRoundUseCase
import com.gousto.kmm.navigation.navModels.CourseNavModel
import com.gousto.kmm.navigation.navModels.GameModeNavModel
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.CourseUiState
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.GameModeUiState
import com.gousto.kmm.presentation.screen.newRound.events.NewRoundScreenUiEvent
import com.gousto.kmm.presentation.screen.newRound.uiState.NewRoundScreenUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class NewRoundScreenViewModel(
    private val newRoundScreenDecorator: NewRoundScreenDecorator,
    private val saveRoundUseCase: SaveRoundUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRoundScreenUiState())
    val uiState: StateFlow<NewRoundScreenUiState> = _uiState

    private val _event = MutableSharedFlow<NewRoundScreenUiEvent>()
    val event = _event.asSharedFlow()

    init {
        getNewRoundScreen()
    }

    private fun getNewRoundScreen() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            _uiState.update { it.copy(isLoading = true) }

            _uiState.update { newRoundScreenDecorator.getUiState() }

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

    fun selectCourse(course: CourseNavModel) {
        _uiState.update { it.copy(selectedCourse = course) }
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
        course: CourseNavModel,
        players: List<UserProfileModel>
    ) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {

            val courseModel = CourseModel(
                name = course.name,
                games = mapGames(course.games),
                location = course.location,
            )
            val roundModel = RoundModel(
                sessionId = sessionId,
                course = courseModel,
                players = players,
                isFinished = false
            )
            saveRoundUseCase.saveRound(
                roundModel
            )
        }
    }

    private fun mapGames(games: List<GameModeNavModel>): List<GameModeModel> {
        return games.map { game ->
            GameModeModel(
                type = game.type,
                holes = game.holes.map { hole ->
                    HoleModel(
                        number = hole.number,
                        par = hole.par
                    )
                }
            )
        }
    }

    private fun generateSessionId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..4)
            .map { chars.random() }
            .joinToString("")
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(NewRoundScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }
}