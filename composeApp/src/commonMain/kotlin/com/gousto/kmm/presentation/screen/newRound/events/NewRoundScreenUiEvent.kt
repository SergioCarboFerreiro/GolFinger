package com.gousto.kmm.presentation.screen.newRound.events

sealed class NewRoundScreenUiEvent {
    data class ShowError(val message: String) : NewRoundScreenUiEvent()

    data class RoundCreated(
        val sessionId: String
    ) : NewRoundScreenUiEvent()
}