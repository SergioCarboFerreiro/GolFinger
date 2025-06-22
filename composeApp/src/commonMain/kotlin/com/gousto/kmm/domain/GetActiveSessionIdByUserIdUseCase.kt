package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.roundRepository.RoundRepository

class GetActiveSessionIdByUserIdUseCase(
    private val roundRepository: RoundRepository
) {
    suspend fun get(userId: String): String? {
        return roundRepository.findRoundSessionIdForUser(userId)
    }
}