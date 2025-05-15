package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.authRepository.AuthRepository
import com.gousto.kmm.data.remote.firebase.userRepository.UserRepository

class RegisterUserUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, handicap: String) {
        val uid = authRepository.registerUser(email, password)
        userRepository.saveUserProfile(uid, name, handicap)
    }
}