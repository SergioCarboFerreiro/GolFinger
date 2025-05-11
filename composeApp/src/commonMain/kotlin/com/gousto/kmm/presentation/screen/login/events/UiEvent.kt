package com.gousto.kmm.presentation.screen.login.events

sealed class UiEvent {
    data object LoginSuccess : UiEvent()
    data class ShowError(val message: String) : UiEvent()
}