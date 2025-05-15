package com.gousto.kmm.presentation.screen.newRound

import androidx.lifecycle.ViewModel
import com.gousto.kmm.presentation.screen.newRound.uiState.Course
import com.gousto.kmm.presentation.screen.newRound.uiState.NewRoundScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NewRoundScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewRoundScreenUiState())
    val uiState: StateFlow<NewRoundScreenUiState> = _uiState

    fun selectCourse(course: Course) {
        _uiState.update { it.copy(selectedCourse = course) }
    }
}