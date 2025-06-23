package com.gousto.kmm.presentation.screen.splash

import com.gousto.kmm.domain.GetActiveRoundSessionIdByUserIdUseCase
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.domain.GetRoundByIdUseCase
import com.gousto.kmm.presentation.screen.splash.state.SplashScreenUiState

class SplashScreenDecorator(
    private val getActiveRoundSessionIdByUserIdUseCase: GetActiveRoundSessionIdByUserIdUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val getRoundByIdUseCase: GetRoundByIdUseCase
) {

    suspend fun getSplashScreenUiState(): SplashScreenUiState {
        val user = getCurrentUserProfileUseCase() ?: return SplashScreenUiState(
            isLoading = false,
            isLoggedIn = false,
            isInActiveRound = true,
            sessionId = null
        )

        val sessionId = getActiveRoundSessionIdByUserIdUseCase.get(user.id)
            ?: return SplashScreenUiState(
                isLoading = false,
                isLoggedIn = true,
                isInActiveRound = true,
                sessionId = null
            )

        val round = getRoundByIdUseCase.getRoundById(sessionId)

        return SplashScreenUiState(
            isLoading = false,
            isLoggedIn = true,
            isInActiveRound = round?.isFinished ?: true,
            sessionId = sessionId
        )
    }

}