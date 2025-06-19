package com.gousto.kmm.presentation.screen.newRound

import com.gousto.kmm.domain.GetAllCoursesUseCase
import com.gousto.kmm.domain.GetAllUsersProfileUseCase
import com.gousto.kmm.presentation.screen.newRound.uiState.NewRoundScreenUiState

class NewRoundScreenDecorator(
    private val getAllUsersUseCase: GetAllUsersProfileUseCase,
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
) {
    suspend fun getUiState(): NewRoundScreenUiState {
        val allCourses = getAllCoursesUseCase()

        return NewRoundScreenUiState(
            courses = allCourses,
            type = allCourses
                .flatMap { it.games.map { it.type } }
                .distinct(),
            players = getAllUsersUseCase(),
            isLoading = false
        )
    }
}