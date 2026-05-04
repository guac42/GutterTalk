package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect

sealed class LeaderboardEffect : GutterTalkEffect() {
object RequestLocationPermission: LeaderboardEffect()
}