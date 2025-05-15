package com.gousto.kmm.presentation.screen.dashboard.events

sealed class DashboardUiEvent {
    data class ShowError(val message: String) : DashboardUiEvent()
    data object LoginSuccess : DashboardUiEvent()
}