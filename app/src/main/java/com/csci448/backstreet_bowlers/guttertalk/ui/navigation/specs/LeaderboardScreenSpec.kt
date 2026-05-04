package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.GutterTalkLeaderboardScreen

object LeaderboardScreenSpec : IScreenSpec {
    private const val LOG_TAG = "448.LeaderboardScreenSpec"

    override val route = "leaderboard"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.leaderboard_screen_title
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        GutterTalkLeaderboardScreen(
            modifier = modifier,
            onUserScoresClick = {
                Log.d(LOG_TAG, "User Scores Callback")
                navController.navigate(ScoresScreenSpec.route)
            },
            onGlobalLeaderboardClick = {
                Log.d(LOG_TAG, "Global Leaderboard Callback")
                navController.navigate(GlobalLeaderboardSpec.route)
            }, // For the future, optional screen
            onLocalLeaderboardClick = {} // For the future, optional screen
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}