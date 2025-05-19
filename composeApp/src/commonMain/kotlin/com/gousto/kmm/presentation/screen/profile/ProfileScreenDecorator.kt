package com.gousto.kmm.presentation.screen.profile

import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.presentation.screen.profile.uiState.ProfileUiState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

class ProfileScreenDecorator(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) {
    suspend fun getProfileUiState(): ProfileUiState? {
        return getCurrentUserProfileUseCase()?.let { profile ->
            ProfileUiState(
                name = profile.name,
                email = Firebase.auth.currentUser?.email ?: "",
                handicap = profile.handicap,
                isLoading = false
            )
        }
    }
}