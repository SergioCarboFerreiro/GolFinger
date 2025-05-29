package com.gousto.kmm.presentation.screen.splash.state

data class SplashScreenUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val isInActiveRound: Boolean = false,
    val sessionId: String? = null,
)