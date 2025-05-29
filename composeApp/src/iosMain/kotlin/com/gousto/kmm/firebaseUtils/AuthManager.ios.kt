package com.gousto.kmm.firebaseUtils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

actual object AuthManager {
    actual fun isUserLoggedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }
}