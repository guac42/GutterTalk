package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.MainActivity
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.GutterTalkLeaderboardScreen
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.LeaderboardViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.LoginViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.ScoresViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.collectInLaunchedEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.LeaderboardEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.LeaderboardIntent

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
        val context = LocalContext.current

        val viewModel = ViewModelProvider(
            store = navBackStackEntry.viewModelStore,
            factory = GutterTalkViewModelFactory(),
            defaultCreationExtras = GutterTalkViewModelFactory.creationExtras(
                navBackStackEntry.defaultViewModelCreationExtras, context
            )
        )[LeaderboardViewModel::class]

        val (state, dispatcher, effects) = viewModel.use(navBackStackEntry)

        effects.collectInLaunchedEffect { effect ->
            when(effect){
                is LeaderboardEffect.RequestLocationPermission -> {
                    val activity = context as? MainActivity
                    activity?.requestLocation()
                }
                null -> {}
            }
        }

        // This code is fucked, im not sure where else to invoke this intent
//        Log.d(LOG_TAG, "Current state of enabled value is: ${state.isLocationAvailable}")
        dispatcher.invoke(LeaderboardIntent.RefreshPermissionStatus)
//        Log.d(LOG_TAG, "Invoked intent")
//        Log.d(LOG_TAG, "New state of enabled value is: ${state.isLocationAvailable}")

        GutterTalkLeaderboardScreen(
            modifier = modifier,
            isLocationEnabled = state.isLocationAvailable,
            onUserScoresClick = {
                Log.d(LOG_TAG, "User Scores Callback")
                navController.navigate(ScoresScreenSpec.route)
            },
            onGlobalLeaderboardClick = {
                Log.d(LOG_TAG, "Global Leaderboard Callback")
                dispatcher.invoke(LeaderboardIntent.OnLeaderboardPressed)
                navController.navigate(GlobalLeaderboardSpec.route)
            }, // For the future, optional screen
            onLocalLeaderboardClick = {
                Log.d(LOG_TAG, "Local Leaderboard Callback")
                navController.navigate(LocalLeaderboardSpec.route)
            } // For the future, optional screen
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}