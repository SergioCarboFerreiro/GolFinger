package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.roundRepository.RoundRepository

class GetActiveCoursesByUserIdUseCase(
    private val roundRepository: RoundRepository
) {
    suspend fun get(userId: String): String? {
        return roundRepository.findActiveRoundForUser(userId)
    }
}