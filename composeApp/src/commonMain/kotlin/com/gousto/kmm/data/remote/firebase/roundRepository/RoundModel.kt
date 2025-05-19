package com.gousto.kmm.data.remote.firebase.roundRepository

import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.presentation.screen.newRound.uiState.Course
import kotlinx.serialization.Serializable

@Serializable
data class RoundModel(
    val sessionId: String,
    val course: Course,
    val players: List<UserProfileModel>
)