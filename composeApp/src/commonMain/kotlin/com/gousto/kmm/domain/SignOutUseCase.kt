package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.authRepository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
    }
}