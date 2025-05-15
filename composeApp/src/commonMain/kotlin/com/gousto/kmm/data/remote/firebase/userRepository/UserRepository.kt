package com.gousto.kmm.data.remote.firebase.userRepository

interface UserRepository {
    suspend fun getCurrentUserProfile(): UserProfileModel?
}