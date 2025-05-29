package com.gousto.kmm.firebaseUtils

actual object AuthManager {
    actual fun isUserLoggedIn(): Boolean {
        return com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null
    }
}