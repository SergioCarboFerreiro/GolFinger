package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.roundRepository.RoundRepository

class GetRoundByIdUseCase(
    private val roundRepository: RoundRepository
) {
    suspend fun getRoundById(sessionId: String) = roundRepository.getRoundById(sessionId)
}