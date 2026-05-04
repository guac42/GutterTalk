package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.GutterTalkScoresScreen

object GlobalLeaderboardSpec : IScreenSpec {
    private const val LOG_TAG = "448.GlobalScreenSpec"

    override val route = "global"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.leaderboard_screen_global
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        Log.d(LOG_TAG, "Navigating to Global Leaderboard Screen")
        GutterTalkScoresScreen(
            modifier = modifier, screen = 1
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}