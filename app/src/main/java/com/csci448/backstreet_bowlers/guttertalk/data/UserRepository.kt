package com.csci448.backstreet_bowlers.guttertalk.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
private const val LOG_TAG = "448.UserRepository"
class UserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val userCollection = firestore.collection("UserInformation")

    suspend fun addUser(user: UserInformation) {
        userCollection.document(user.PlayerID).set(user).await()
    }

    suspend fun getUser(userId: String): UserInformation? {
        Log.d(LOG_TAG, "Attempting to retrieve information for user: $userId")
        val userInformation = userCollection.document(userId).get().await().toObject(UserInformation::class.java)

        if(userInformation != null){
            Log.d(LOG_TAG, "User Information found: $userInformation")
        }else{
            Log.d(LOG_TAG, "No Information found: $userInformation")
        }
        return userInformation
    }

    suspend fun updateUserStats(userId: String, score: Int, strikes: Int, spares: Int) {
        val userRef = userCollection.document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentGames = snapshot.getLong("gamesPlayed") ?: 0
            val currentScore = snapshot.getLong("lifetimeScore") ?: 0
            val currentStrikes = snapshot.getLong("lifetimeStrikes") ?: 0
            val currentSpares = snapshot.getLong("lifetimeSpares") ?: 0

            transaction.update(userRef, "gamesPlayed", currentGames + 1)
            transaction.update(userRef, "lifetimeScore", currentScore + score)
            transaction.update(userRef, "lifetimeStrikes", currentStrikes + strikes)
            transaction.update(userRef, "lifetimeSpares", currentSpares + spares)
        }.await()
    }

    fun getLeaderboard(limit: Long = 10, location: String? = null): Flow<List<UserInformation>> = flow {
        var query: Query = if (location != null) {
            userCollection.whereEqualTo("location", location)
        } else {
            userCollection
        }

        val snapshot = query
            .orderBy("lifetimeScore", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()

        emit(snapshot.toObjects(UserInformation::class.java))
    }
}