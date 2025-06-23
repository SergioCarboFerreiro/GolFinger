package com.gousto.kmm.data.remote.firebase.roundRepository

interface RoundRepository {
    suspend fun saveRound(round: RoundModel)
    suspend fun getRoundById(sessionId: String): RoundModel?
    suspend fun findActiveRoundSessionIdForUser(userId: String): String?
    suspend fun getAllRounds(): List<RoundModel>
    suspend fun getAllRoundsForUser(userId: String): List<RoundModel>
}