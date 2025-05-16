package com.gousto.kmm.presentation.screen.newRound.events

import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.presentation.screen.newRound.uiState.Course

sealed class NewRoundScreenUiEvent {
    data class ShowError(val message: String) : NewRoundScreenUiEvent()

    data class RoundCreated(
        val sessionId: String,
        val course: Course,
        val players: List<UserProfileModel>
    ) : NewRoundScreenUiEvent()
}