package com.gousto.kmm.presentation.screen.userStats

import com.gousto.kmm.domain.GetAllRoundsForUserUseCase
import com.gousto.kmm.presentation.screen.userStats.state.StatsPerType
import com.gousto.kmm.presentation.screen.userStats.state.UserStatsScreenUiState
import com.gousto.kmm.presentation.screen.userStats.state.UserStatsUiState

class UserStatsScreenDecorator(
    private val getAllRoundsForUserUseCase: GetAllRoundsForUserUseCase
) {

    suspend fun getStatsForUser(userId: String): UserStatsScreenUiState {
        val rounds = getAllRoundsForUserUseCase.get(userId)

        val strokeSumsPerRound = mutableListOf<Int>()
        var birdies = 0
        var pars = 0
        var bogeys = 0

        val groupedStats = mutableMapOf<String, MutableList<Int>>() // tipo → strokes por ronda

        rounds.forEach { round ->
            val userScores = round.scores.filter { it.playerId == userId }

            val scoresGroupedByType = round.course.games.associate { game ->
                val parByHole = game.holes.associateBy({ it.number }, { it.par })
                val scoresForType = userScores.filter { parByHole.containsKey(it.hole) }

                var strokesThisGame = 0

                scoresForType.forEach { entry ->
                    val strokes = entry.strokes.toIntOrNull() ?: return@forEach
                    strokesThisGame += strokes
                    val par = parByHole[entry.hole] ?: return@forEach

                    when {
                        strokes < par -> birdies++
                        strokes == par -> pars++
                        strokes > par -> bogeys++
                    }
                }

                game.type to strokesThisGame.takeIf { it > 0 }
            }.filterValues { it != null }.mapValues { it.value!! }

            scoresGroupedByType.forEach { (type, strokes) ->
                groupedStats.getOrPut(type) { mutableListOf() }.add(strokes)
                strokeSumsPerRound.add(strokes)
            }
        }

        val average = strokeSumsPerRound.takeIf { it.isNotEmpty() }?.average() ?: 0.0
        val best = strokeSumsPerRound.minOrNull() ?: 0

        val statsByType = groupedStats.mapValues { (_, strokesList) ->
            StatsPerType(
                rounds = strokesList.size,
                averageStrokes = strokesList.average(),
                bestRound = strokesList.minOrNull() ?: 0
            )
        }

        return UserStatsScreenUiState(
            isLoading = false,
            stats = UserStatsUiState(
                totalRounds = strokeSumsPerRound.size,
                averageStrokes = average,
                bestRound = best,
                totalBirdies = birdies,
                totalPars = pars,
                totalBogeys = bogeys,
                statsByType = statsByType
            )
        )
    }
}