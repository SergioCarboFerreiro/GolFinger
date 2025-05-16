package com.gousto.kmm.data.remote.firebase.authRepository

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): String
}