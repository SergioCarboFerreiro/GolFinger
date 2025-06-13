package com.gousto.kmm.presentation.screen.splash

import com.gousto.kmm.domain.GetActiveCoursesByUserIdUseCase
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.presentation.screen.splash.state.SplashScreenUiState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

class SplashScreenDecorator(
    private val getActiveCoursesByUserIdUseCase: GetActiveCoursesByUserIdUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
) {

    suspend fun getSplashScreenUiState(): SplashScreenUiState {
        getCurrentUserProfileUseCase()?.let { user ->
            getActiveCoursesByUserIdUseCase.get(user.id)?.let { sessionId ->
                return SplashScreenUiState(
                    isLoading = false,
                    isLoggedIn = true,
                    isInActiveRound = true,
                    sessionId = sessionId
                )
            } ?: return SplashScreenUiState(
                isLoading = false,
                isLoggedIn = true,
                isInActiveRound = false,
                sessionId = null
            )

        } ?: return SplashScreenUiState(
            isLoading = false,
            isLoggedIn = false,
            isInActiveRound = false,
            sessionId = null
        )

    }

}