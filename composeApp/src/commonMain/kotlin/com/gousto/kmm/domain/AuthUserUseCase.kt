package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.authRepository.AuthRepository
import dev.gitlive.firebase.auth.AuthResult

class AuthUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend fun authUser(email: String, password: String) : AuthResult {
       return authRepository.authUser(email, password)
    }
}