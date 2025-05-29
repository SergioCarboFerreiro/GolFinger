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

    override suspend fun findActiveRoundForUser(userId: String): String? {
        val rounds = Firebase.firestore.collection(ROUNDS).get()

        val match = rounds.documents.firstOrNull { doc ->
            val round = doc.data<RoundModel>()          // deserializa a tu modelo
            round.players.any { it.id == userId }       // comprueba el uid
        }

        return match?.id                                // id del documento = sessionId
    }

    companion object {
        const val ROUNDS = "rounds"
        const val PLAYERS = "players"
        const val USER_ID = "id"
        const val SESSION_ID = "sessionId"
    }
}