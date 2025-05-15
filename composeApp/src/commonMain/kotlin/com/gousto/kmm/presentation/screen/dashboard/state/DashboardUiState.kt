package com.gousto.kmm.presentation.screen.dashboard.state

data class DashboardUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)