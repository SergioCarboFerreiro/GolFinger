package com.gousto.kmm.data.remote.firebase.authRepository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.auth

class AuthRepositoryImpl : AuthRepository {
    override suspend fun registerUser(email: String, password: String): String {
        val result = Firebase.auth.createUserWithEmailAndPassword(email, password)
        return result.user?.uid ?: throw Exception("No se pudo crear el usuario")
    }

    override suspend fun authUser(email: String, password: String): AuthResult {
        return Firebase.auth.signInWithEmailAndPassword(email, password)
    }
}