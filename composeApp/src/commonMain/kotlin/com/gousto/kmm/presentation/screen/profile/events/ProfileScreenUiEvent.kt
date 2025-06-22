package com.gousto.kmm.presentation.screen.profile.events

import com.gousto.kmm.presentation.screen.dashboard.events.DashboardUiEvent

sealed class ProfileScreenUiEvent {
    data class ShowError(val message: String) : ProfileScreenUiEvent()
    data object NavigateToLoginScreen : ProfileScreenUiEvent()
}
