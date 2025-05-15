package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserRepository

class GetCurrentUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): UserProfileModel? {
        return userRepository.getCurrentUserProfile()
    }
}