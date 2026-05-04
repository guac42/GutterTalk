package com.csci448.backstreet_bowlers.guttertalk.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BowlingScoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val scoresCollection = firestore.collection("UserScores")

    suspend fun addScore(score: BowlingScore) {
        scoresCollection.document(score.id).set(score).await()
    }

    suspend fun getScore(scoreId: String): BowlingScore? {
        return scoresCollection.document(scoreId).get().await().toObject(BowlingScore::class.java)
    }

    suspend fun deleteScore(scoreId: String) {
        scoresCollection.document(scoreId).delete().await()
    }

    fun getUserScores(userId: String): Flow<List<BowlingScore>> = flow {
        Log.d("BowlingScoreRepository", "Fetching user scores for user ID: $userId")
        val snapshot = scoresCollection
            .whereEqualTo("PlayerID", userId)
            .get()
            .await()

        Log.d("FirestoreData", "Total documents found: ${snapshot.size()}")

        // 2. Map the documents to your class
        val scores = snapshot.documents.mapNotNull { doc ->
            val bowlingScore = doc.toObject(BowlingScore::class.java)
            bowlingScore?.copy(
                id = doc.id,
                score = bowlingScore.scores[9],
                frameNumber = 10
            )
        }

        // 3. Log each document as a Map (JSON-like structure)
        snapshot.documents.forEachIndexed { index, doc ->
            Log.d("FirestoreData", "Doc $index ID: ${doc.id} => Data: ${doc.data}")
        }

        emit(scores)

        // Replace with your actual collection and document IDs
                /*
        val docRef = scoresCollection.document("IuZcpoSekHRKI7iXQ2fA3fl0Qwu2")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // document.data returns a Map<String, Any>
                    val data = document.data
                    Log.d("Tag", "Document Data (Map format): $data")

                    // If you want it to look exactly like a JSON string in your logs:
                    // You could use a library like Gson or just print the map
                    Log.d("Tag", "Fields: ${data?.keys}")
                } else {
                    Log.d("Tag", "No such document exists!")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Tag", "Error getting document: ", exception)
            }*/
    }

    suspend fun updateScore(score: BowlingScore) {
        scoresCollection.document(score.id).set(score).await()
    }
}