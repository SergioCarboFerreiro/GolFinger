package com.gousto.kmm.data.remote.firebase.userRepository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class UserRepositoryImpl : UserRepository {

    override suspend fun getCurrentUserProfile(): UserProfileModel? {
        val uid = Firebase.auth.currentUser?.uid ?: return null

        val snapshot = Firebase.firestore
            .collection(USERS)
            .document(uid)
            .get()

        val name = snapshot.get<String>(NAME)
        val handicap = snapshot.get<String>(HANDICAP)

        return UserProfileModel(name, handicap)
    }

    override suspend fun saveUserProfile(uid: String, name: String, handicap: String) {
        Firebase.firestore.collection(USERS)
            .document(uid)
            .set(mapOf(NAME to name, HANDICAP to handicap))
    }

    companion object {
        const val NAME = "name"
        const val HANDICAP = "handicap"
        const val USERS = "users"
    }
}