package com.gousto.kmm.presentation.screen.userStats

import com.gousto.kmm.domain.GetRoundForUserUseCase
import com.gousto.kmm.presentation.screen.userStats.state.UserStatsScreenUiState
import com.gousto.kmm.presentation.screen.userStats.state.UserStatsUiState

class UserStatsScreenDecorator(
    private val getRoundForUserUseCase: GetRoundForUserUseCase
) {

    suspend fun getStatsForUser(userId: String): UserStatsScreenUiState {
        val rounds = getRoundForUserUseCase.get(userId)

        val strokeSumsPerRound = mutableListOf<Int>()
        var birdies = 0
        var pars = 0
        var bogeys = 0

        rounds.forEach { round ->
            val parByHole = round.course.games
                .firstOrNull() // puedes elegir mejor estrategia luego
                ?.holes
                ?.associateBy({ it.number }, { it.par }) ?: emptyMap()

            val userScores = round.scores.filter { it.playerId == userId }

            var strokesThisRound = 0

            userScores.forEach { entry ->
                val strokes = entry.strokes.toIntOrNull() ?: return@forEach
                strokesThisRound += strokes
                val par = parByHole[entry.hole] ?: return@forEach

                when {
                    strokes < par -> birdies++
                    strokes == par -> pars++
                    strokes > par -> bogeys++
                }
            }

            if (strokesThisRound > 0) {
                strokeSumsPerRound.add(strokesThisRound)
            }
        }

        val average = if (strokeSumsPerRound.isNotEmpty()) strokeSumsPerRound.average() else 0.0
        val best = strokeSumsPerRound.minOrNull() ?: 0

        return UserStatsScreenUiState(
            isLoading = false,
            stats = UserStatsUiState(
                totalRounds = strokeSumsPerRound.size,
                averageStrokes = average,
                bestRound = best,
                totalBirdies = birdies,
                totalPars = pars,
                totalBogeys = bogeys
            )
        )
    }
}