package com.gousto.kmm.presentation.screen.round

import com.gousto.kmm.domain.GetAllCoursesUseCase
import com.gousto.kmm.domain.GetAllUsersProfileUseCase
import com.gousto.kmm.domain.GetRoundByIdUseCase
import com.gousto.kmm.presentation.screen.round.state.RoundScreenUiState

class RoundScreenDecorator(
    private val getRoundByIdUseCase: GetRoundByIdUseCase,
    private val getAllCoursesUseCase: GetAllCoursesUseCase
) {

    suspend fun getRoundById(sessionId: String): RoundScreenUiState? {
        val round = getRoundByIdUseCase.getRoundById(sessionId) ?: return null

        val allCourses = getAllCoursesUseCase()
        val matchingCourse = allCourses.find { it.name == round.course.name }

        return RoundScreenUiState(
            sessionId = round.sessionId,
            course = matchingCourse ?: round.course,
            players = round.players,
            scores = emptyMap(),
            isLoading = false
        )
    }
}