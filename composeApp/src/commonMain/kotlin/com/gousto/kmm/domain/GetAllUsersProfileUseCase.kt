package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserRepository

class GetAllUsersProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): List<UserProfileModel> = userRepository.getAllUsers()
}