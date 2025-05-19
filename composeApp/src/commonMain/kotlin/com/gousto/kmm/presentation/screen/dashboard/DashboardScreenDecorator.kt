package com.gousto.kmm.presentation.screen.dashboard

import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.presentation.screen.dashboard.state.DashboardUiState

class DashboardScreenDecorator(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) {
    suspend fun getDashboardUiState(): DashboardUiState {
        val user = getCurrentUserProfileUseCase()
        return DashboardUiState(
            name = user?.name ?: "Usuario",
            isLoading = false
        )
    }
}
