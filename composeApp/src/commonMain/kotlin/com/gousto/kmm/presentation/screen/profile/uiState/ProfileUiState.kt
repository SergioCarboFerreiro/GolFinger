package com.gousto.kmm.presentation.screen.profile.uiState

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val handicap: String = "",
    val isLoading: Boolean = true
)