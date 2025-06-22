package com.gousto.kmm.presentation.screen.userStats.events

import com.gousto.kmm.presentation.screen.splash.events.SplashScreenUiEvent

sealed class UserStatsScreenUiEvent {
    data class ShowError(val message: String) : UserStatsScreenUiEvent()
}