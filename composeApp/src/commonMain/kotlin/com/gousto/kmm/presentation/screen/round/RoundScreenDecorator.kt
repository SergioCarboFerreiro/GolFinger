package com.gousto.kmm.presentation.screen.round

import com.gousto.kmm.data.remote.firebase.roundRepository.ScoreEntry
import com.gousto.kmm.domain.GetAllCoursesUseCase
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
        val scoreMap = round.scores.toScoreMap()


        return RoundScreenUiState(
            sessionId = round.sessionId,
            course = matchingCourse ?: round.course,
            players = round.players,
            scores = scoreMap,
            isLoading = false
        )
    }

    private fun List<ScoreEntry>.toScoreMap(): Map<Pair<Int, String>, String> =
        associate { Pair(it.hole, it.playerId) to it.strokes }
}