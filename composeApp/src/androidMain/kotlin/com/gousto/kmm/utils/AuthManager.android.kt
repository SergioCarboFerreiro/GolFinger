package com.gousto.kmm.utils

actual object AuthManager {
    actual fun isUserLoggedIn(): Boolean {
        return com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null
    }
}