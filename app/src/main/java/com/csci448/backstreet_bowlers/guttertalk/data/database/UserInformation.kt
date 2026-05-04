package com.csci448.backstreet_bowlers.guttertalk.data.database

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInformation(
    @DocumentId val PlayerID: String = "",
    val username: String = "",
    val gamesPlayed: Int = 0,
    val lifetimeScore: Int = 0,
    val lifetimeSpares: Int = 0,
    val lifetimeStrikes: Int = 0,
    val city: String? = null,
    val adminDistrict: String? = null,
    val country: String? = null,
    val highestScore: Int = 0
)