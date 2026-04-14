package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.main.GutterTalkMainScreen

data object MainScreenSpec : IScreenSpec {
    private const val LOG_TAG = "448.MainScreenSpec"

    override val route = "main"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.app_name
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        GutterTalkMainScreen(
            modifier = modifier,
            onPlayClick = {
                navController.navigate(GameScreenSpec.route)
            },
            onLeaderBoardClick = {
                navController.navigate(LeaderboardScreenSpec.route)
            },
            onSettingsClick = {
                navController.navigate(SettingsScreenSpec.route)
            }

        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}