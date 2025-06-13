package com.gousto.kmm.data.remote.firebase.roundRepository

interface RoundRepository {
    suspend fun saveRound(round: RoundModel)
    suspend fun getRoundById(sessionId: String): RoundModel?
    suspend fun findActiveRoundForUser(userId: String): String?
}