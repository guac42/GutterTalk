package com.csci448.backstreet_bowlers.guttertalk.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInformation(
    @DocumentId val userId: String,
    val username: String = "",
    val gamesPlayed: Int = 0,
    val lifetimeScore: Int = 0,
    val lifetimeSpares: Int = 0,
    val lifetimeStrikes: Int = 0,
    val location: String? = null
)