package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.lane.GutterTalkLaneScreen

object LaneScreenSpec : IScreenSpec {
    private const val LOG_TAG = "448.LaneScreenSpec"

    override val route = "lane"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.lane_screen_title
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        GutterTalkLaneScreen(
            modifier = modifier
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}