package com.gousto.kmm.presentation.screen.newRound.events

import com.gousto.kmm.presentation.screen.newRound.uiState.NewRoundScreenUiState

sealed class NewRoundScreenEvent {
    data object LoginSuccess : NewRoundScreenEvent()
    data class ShowError(val message: String) : NewRoundScreenEvent()
}