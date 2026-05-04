package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.data.database.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.GutterTalkScoresScreen
import java.util.UUID

object ScoresScreenSpec : IScreenSpec {
    private const val LOG_TAG = "448.ScoresScreenSpec"

    override val route = "scores"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.scores_screen_title
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        Log.d(LOG_TAG, "Navigating to User Scores Screen")
        GutterTalkScoresScreen(
            modifier = modifier,
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}