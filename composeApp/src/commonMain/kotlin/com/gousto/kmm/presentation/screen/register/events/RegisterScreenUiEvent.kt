package com.gousto.kmm.presentation.screen.register.events

sealed class RegisterScreenUiEvent {
    data class ShowError(val message: String) : RegisterScreenUiEvent()
}