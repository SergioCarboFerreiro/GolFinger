package com.gousto.kmm.data.remote.firebase.userRepository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class UserRepositoryImpl : UserRepository {

    override suspend fun getCurrentUserProfile(): UserProfileModel? {
        val uid = Firebase.auth.currentUser?.uid ?: return null

        return try {
            val snapshot = Firebase.firestore
                .collection("users")
                .document(uid)
                .get()

            val name = snapshot.get<String>("name") ?: ""
            val handicap = snapshot.get<String>("handicap") ?: ""

            UserProfileModel(name, handicap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}