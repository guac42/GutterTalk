package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent

import android.location.Location
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.GutterTalkEffect

sealed class LeaderboardIntent : GutterTalkIntent() {
    class updateLocation (val location: Location) : LeaderboardIntent()
//    object CheckLocationPermission : LeaderboardIntent()
    object OnLeaderboardPressed : LeaderboardIntent()
    object RefreshPermissionStatus: LeaderboardIntent()
}