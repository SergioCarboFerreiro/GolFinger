package com.gousto.kmm.data.remote.firebase.roundRepository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class RoundRepositoryImpl : RoundRepository {

    override suspend fun saveRound(round: RoundModel) {
        try {
            Firebase.firestore
                .collection(ROUNDS)
                .document(round.sessionId)
                .set(round)
        } catch (
            e: Exception
        ) {
            throw RuntimeException("Failed to save round: ${e.message}", e)
        }

    }

    override suspend fun getRoundById(sessionId: String): RoundModel {
        try {
            val snapshot = Firebase.firestore
                .collection(ROUNDS)
                .document(sessionId)
                .get()
            val round = snapshot.data<RoundModel>()
            return round
        } catch (
            e: Exception
        ) {
            throw RuntimeException("Failed to save round: ${e.message}", e)
        }
    }

    override suspend fun findRoundSessionIdForUser(userId: String): String? {
        try {
            val rounds = Firebase.firestore.collection(ROUNDS).get()

            val match = rounds.documents.firstOrNull { doc ->
                val round = doc.data<RoundModel>()
                round.players.any { it.id == userId }
            }
            return match?.id
        } catch (
            e: Exception
        ) {
            throw RuntimeException("Failed to save round: ${e.message}", e)
        }
    }

    override suspend fun getAllRounds(): List<RoundModel> {
        try {
            val snapshot = Firebase.firestore
                .collection("rounds")
                .where("isFinished", true)
                .get()

            return snapshot.documents.map { doc ->
                doc.data<RoundModel>()
            }
        } catch (
            e: Exception
        ) {
            throw RuntimeException("Failed to get all rounds: ${e.message}", e)
        }
    }

    override suspend fun getRoundsForUser(userId: String): List<RoundModel> {
        return getAllRounds().filter { round ->
            round.players.any { it.id == userId }
        }
    }



    companion object {
        const val ROUNDS = "rounds"
        const val PLAYERS = "players"
        const val USER_ID = "id"
        const val SESSION_ID = "sessionId"
    }
}