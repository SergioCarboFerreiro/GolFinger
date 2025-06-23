package com.gousto.kmm.presentation.screen.userStats.state

data class UserStatsScreenUiState(
    val isLoading: Boolean = false,
    val stats: UserStatsUiState? = null
)

data class UserStatsUiState(
    val totalRounds: Int,
    val averageStrokes: Double,
    val bestRound: Int,
    val totalBirdies: Int,
    val totalPars: Int,
    val totalBogeys: Int,
    val statsByType: Map<String, StatsPerType> = emptyMap() // 👈 añadido
)

data class StatsPerType(
    val rounds: Int,
    val averageStrokes: Double,
    val bestRound: Int
)