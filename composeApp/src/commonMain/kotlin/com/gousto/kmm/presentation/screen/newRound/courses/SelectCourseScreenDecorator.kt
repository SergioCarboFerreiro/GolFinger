package com.gousto.kmm.presentation.screen.newRound.courses

import com.gousto.kmm.domain.GetAllCoursesUseCase
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.CourseUiState
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.GameModeUiState
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.HoleUiState
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.SelectCourseScreenUiState

class SelectCourseScreenDecorator(
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
) {
    suspend fun getSelectCourseScreenUiState(): SelectCourseScreenUiState {
        return SelectCourseScreenUiState(
            isLoading = false,
            errorMessage = null,
            courses = getAllCoursesUseCase().map { course ->
                CourseUiState(
                    name = course.name,
                    location = course.location,
                    games = course.games.map { game ->
                        GameModeUiState(
                            type = game.type,
                            holes = game.holes.map { hole ->
                                HoleUiState(
                                    number = hole.number,
                                    par = hole.par
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}