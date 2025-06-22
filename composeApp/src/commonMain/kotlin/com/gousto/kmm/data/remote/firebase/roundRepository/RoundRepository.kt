package com.gousto.kmm.data.remote.firebase.roundRepository

interface RoundRepository {
    suspend fun saveRound(round: RoundModel)
    suspend fun getRoundById(sessionId: String): RoundModel?
    suspend fun findRoundSessionIdForUser(userId: String): String?
    suspend fun getAllRounds(): List<RoundModel>
    suspend fun getRoundsForUser(userId: String): List<RoundModel>
}