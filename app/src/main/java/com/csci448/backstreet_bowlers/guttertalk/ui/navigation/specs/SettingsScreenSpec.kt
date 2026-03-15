package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.settings.GutterTalkSettingsScreen

data object SettingsScreenSpec : IScreenSpec {
    private const val LOG_TAG = "448.SettingsScreenSpec"

    override val route = "settings"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.settings_screen_title
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        GutterTalkSettingsScreen(
            modifier = modifier,
            onToggleMusicClick = {},
            onMusicSliderClick = {},
            onToggleInsultClick = {}
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}