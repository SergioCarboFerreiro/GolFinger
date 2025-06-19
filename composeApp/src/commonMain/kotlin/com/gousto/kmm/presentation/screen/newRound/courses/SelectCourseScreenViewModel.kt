package com.gousto.kmm.presentation.screen.newRound.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gousto.kmm.presentation.screen.newRound.courses.events.SelectCourseScreenUiEvent
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.SelectCourseScreenUiState
import com.gousto.kmm.presentation.screen.newRound.events.NewRoundScreenUiEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectCourseScreenViewModel(
    private val selectCourseScreenDecorator: SelectCourseScreenDecorator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectCourseScreenUiState())
    val uiState: StateFlow<SelectCourseScreenUiState> = _uiState

    private val _event = MutableSharedFlow<SelectCourseScreenUiEvent>()
    val event = _event.asSharedFlow()

    init {
        fetchCourses()
    }

//    fun selectCourse(course: CourseModel) {
//        _uiState.update { it.copy(selectedCourse = course) }
//    }

    private fun fetchCourses() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                viewModelScope.launch { handleError(error) }
            }
        ) {
            _uiState.update { it.copy(isLoading = true) }

            _uiState.update {  selectCourseScreenDecorator.getSelectCourseScreenUiState()}

        }
    }

    private suspend fun handleError(error: Throwable) {
        _event.emit(SelectCourseScreenUiEvent.ShowError(error.message ?: "Error al cargar el perfil"))
        _uiState.update { it.copy(isLoading = false) }
    }

//    fun onStartRoundClicked() {
//        viewModelScope.launch(
//            CoroutineExceptionHandler { _, error ->
//                viewModelScope.launch { handleError(error) }
//            }
//        ) {
//            val course = _uiState.value.selectedCourse
//            val selectedPlayers = _uiState.value.selectedPlayers
//            val selectedPlayerModels = _uiState.value.players.filter { it.id in selectedPlayers }
//
//            if (course != null && selectedPlayers.isNotEmpty()) {
//                val sessionId = generateSessionId()
//                saveRoundSession(sessionId, course, selectedPlayerModels)
//                _event.emit(
//                    NewRoundScreenUiEvent.RoundCreated(sessionId = sessionId)
//                )
//            } else {
//                _event.emit(NewRoundScreenUiEvent.ShowError("Selecciona un campo y al menos un jugador."))
//            }
//        }
//    }
}