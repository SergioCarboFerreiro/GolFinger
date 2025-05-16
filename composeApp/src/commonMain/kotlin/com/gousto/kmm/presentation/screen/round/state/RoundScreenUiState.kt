package com.gousto.kmm.presentation.screen.round.state

import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.presentation.screen.newRound.uiState.Course

data class RoundScreenUiState(
    val sessionId: String = "",
    val course: Course? = null,
    val players: List<UserProfileModel> = emptyList()
)