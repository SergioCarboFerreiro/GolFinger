package com.gousto.kmm.presentation.screen.newRound.events

sealed class NewRoundScreenUiEvent {
    data object LoginSuccess : NewRoundScreenUiEvent()
    data class ShowError(val message: String) : NewRoundScreenUiEvent()
}