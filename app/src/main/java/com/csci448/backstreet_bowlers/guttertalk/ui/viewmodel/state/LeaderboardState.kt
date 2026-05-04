package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state

import android.location.Location
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardState(
    @Contextual
    val location: Location? = null,
    val city: String = "",
    val adminDistrict: String = "",
    val countryCode: String = "",
    val isLocationAvailable: Boolean = false
) : GutterTalkState