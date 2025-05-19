package com.gousto.kmm.data.remote.firebase.roundRepository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class RoundRepositoryImpl : RoundRepository {

    override suspend fun saveRound(round: RoundModel) {
        Firebase.firestore
            .collection(ROUNDS)
            .document(round.sessionId)
            .set(round)
    }

    override suspend fun getRoundById(sessionId: String): RoundModel {
        val snapshot = Firebase.firestore
            .collection(ROUNDS)
            .document(sessionId)
            .get()

        return snapshot.data<RoundModel>()
    }

    companion object {
        const val ROUNDS = "rounds"
    }
}