package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.GutterTalkScoresScreen
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.LeaderboardViewModel

object LocalLeaderboardSpec : IScreenSpec {
    private const val LOG_TAG = "448.ScoresScreenSpec"

    override val route = "local"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.leaderboard_screen_local
    override fun buildRoute(vararg args: String?) = route

    @Composable
    override fun Content(
        modifier: Modifier,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry
    ) {
        val context = LocalContext.current

        val viewModel = ViewModelProvider(
            store = navBackStackEntry.viewModelStore,
            factory = GutterTalkViewModelFactory(),
            defaultCreationExtras = GutterTalkViewModelFactory.creationExtras(
                navBackStackEntry.defaultViewModelCreationExtras, context
            )
        )[LeaderboardViewModel::class]

        val (state, dispatcher, effects) = viewModel.use(navBackStackEntry)
        Log.d(LOG_TAG, "Screen 2 processing, should search on country: ${state.countryCode}")

        GutterTalkScoresScreen(
            modifier = modifier, screen = 2, userLocation = state.countryCode
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}