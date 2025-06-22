package com.gousto.kmm.data.remote.firebase.authRepository

import dev.gitlive.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): String
    suspend fun authUser(email: String, password: String) : AuthResult
    suspend fun signOut()
}