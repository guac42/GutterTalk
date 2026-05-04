package com.csci448.backstreet_bowlers.guttertalk.data.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val LOG_TAG = "448.UserRepository"
class UserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val userCollection = firestore.collection("UserInformation")
    var createUserRecord : Boolean = false

    suspend fun addUser(user: UserInformation) {
        userCollection.document(user.PlayerID).set(user).await()
    }
    fun checkUserExists(userId: String) {
        // I think Dr. Paone would shoot me if he saw ts
        fun performCheck() = runBlocking{
            launch(Dispatchers.Default){
                val snapshot = userCollection
                    .document(userId)
                    .get()
                    .await()
                if(!snapshot.exists()){
                    Log.d(LOG_TAG, "User does not exist - need to create new record")
                    var username:String? = createUsername(com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email)
                    if(username == null){
                        Log.d(LOG_TAG, "Error processing account creation, record creation terminated")
                        throw Exception("Error processing account creation, record creation terminated")
                    }else{
                        addUser(UserInformation(
                            PlayerID = userId,
                            username = username,
                        ))
                    }
                }else{
                    Log.d(LOG_TAG, "User already exists")
                }
            }
        }
        performCheck()
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
    suspend fun updateUserLocation(userId: String, city: String, adminDistrict: String, country: String) {
        val userRef = userCollection.document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            transaction.update(userRef, "city", city)
            transaction.update(userRef, "adminDistrict", adminDistrict)
            transaction.update(userRef, "country", country)
        }.await()
    }

    fun getLeaderboard(limit: Long = 10, location: String? = null): Flow<List<UserInformation>> = flow {
        Log.d(LOG_TAG, "Searching for players: where country: ${location}")

        var query: Query = if (location != null) {
            userCollection.whereEqualTo("country", location)

        } else {
            userCollection
        }

        val snapshot = query
            .orderBy("lifetimeScore", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()

        Log.d(LOG_TAG, "Total documents found: ${snapshot.size()}")
        snapshot.documents.forEachIndexed { index, doc ->
            Log.d(LOG_TAG, "Doc $index ID: ${doc.id} => Data: ${doc.data}")
        }

        emit(snapshot.toObjects(UserInformation::class.java))
    }

    fun createUsername(email:String?):String?{
        Log.d(LOG_TAG, "Creating Username")
        if(email != null){
            return email.substringBefore("@")
        }
        return null
    }
}