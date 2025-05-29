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
        val id = snapshot.get<String>(ID)

        return UserProfileModel(id = id, name = name, handicap = handicap)
    }

    override suspend fun saveUserProfile(uid: String, name: String, handicap: String) {
        Firebase.firestore.collection(USERS)
            .document(uid)
            .set(mapOf(ID to uid, NAME to name, HANDICAP to handicap))
    }

    override suspend fun getAllUsers(): List<UserProfileModel> {
        val snapshot = Firebase.firestore.collection("users").get()
        return snapshot.documents.mapNotNull { doc ->
            val name = doc.get<String>(NAME)
            val handicap = doc.get<String>(HANDICAP)
            val id = doc.get<String>(ID)
            UserProfileModel(id = id, name = name, handicap = handicap)
        }
    }

    companion object {
        const val NAME = "name"
        const val HANDICAP = "handicap"
        const val USERS = "users"
        const val EMAIL = "email"
        const val ID = "id"
    }
}