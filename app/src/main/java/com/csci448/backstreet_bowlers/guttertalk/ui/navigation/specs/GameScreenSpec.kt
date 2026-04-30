package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.game.GutterTalkLaneScreen
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GameViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.collectInLaunchedEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.GameEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.GameIntent

object GameScreenSpec : IScreenSpec {
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
        val context = LocalContext.current

        val viewModel = ViewModelProvider(
            store = navBackStackEntry.viewModelStore,
            factory = GutterTalkViewModelFactory(),
            defaultCreationExtras = GutterTalkViewModelFactory.creationExtras(
                navBackStackEntry.defaultViewModelCreationExtras, context
            )
        )[GameViewModel::class]

        val (state, dispatcher, effects) = viewModel.use(navBackStackEntry)

        effects.collectInLaunchedEffect {
            when(it) {
                GameEffect.Spare -> {}
                GameEffect.Strike -> {}
                null -> {}
            }
        }

        GutterTalkLaneScreen(
            modifier = modifier,
            onBallSettled = {
                dispatcher.invoke(GameIntent.BallSettled(it))
            }
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}