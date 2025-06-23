package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.roundRepository.RoundModel
import com.gousto.kmm.data.remote.firebase.roundRepository.RoundRepository

class GetAllRoundsForUserUseCase(
    private val roundRepository: RoundRepository
) {
    suspend fun get(userId: String): List<RoundModel> {
        return roundRepository.getAllRoundsForUser(userId)
    }
}