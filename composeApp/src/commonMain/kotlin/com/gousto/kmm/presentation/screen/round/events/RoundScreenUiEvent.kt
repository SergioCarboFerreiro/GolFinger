package com.gousto.kmm.presentation.screen.round.events

sealed class RoundScreenUiEvent {
    data class ShowError(val message: String) : RoundScreenUiEvent()
}
