package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.roundRepository.RoundModel
import com.gousto.kmm.data.remote.firebase.roundRepository.RoundRepository

class SaveRoundUseCase(
    private val roundRepository: RoundRepository,
) {
    suspend fun saveRound(round: RoundModel) {
        roundRepository.saveRound(round)
    }
}