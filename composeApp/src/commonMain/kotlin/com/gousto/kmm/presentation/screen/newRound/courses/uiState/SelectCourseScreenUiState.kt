package com.gousto.kmm.presentation.screen.newRound.courses.uiState

data class SelectCourseScreenUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val courses: List<CourseUiState> = emptyList(),
    val selectedCourseId: String? = null
)

data class CourseUiState(
    val name: String,
    val location: String,
    val games: List<GameModeUiState>
)

data class GameModeUiState(
    val type: String,
    val holes: List<HoleUiState>
)

data class HoleUiState(
    val number: Int,
    val par: Int
)