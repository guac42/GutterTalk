package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.game.GutterTalkLaneScreen
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GameViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.SettingsViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.collectInLaunchedEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.GameEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

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
        val activity = context as androidx.activity.ComponentActivity
        val settingsViewModel: SettingsViewModel = viewModel(
            viewModelStoreOwner = activity,
            factory = GutterTalkViewModelFactory(),
            extras = GutterTalkViewModelFactory.creationExtras(
                activity.defaultViewModelCreationExtras,
                context
            )
        )
        val settingsState by settingsViewModel.stateFlow.collectAsState()

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
                is GameEffect.Insult -> {
                    Log.d(LOG_TAG, "Collecting insult effect")
                    if (settingsState.isInsultsOn) {
                        Toast.makeText(context, it.insult, Toast.LENGTH_LONG).show()
                    }
                }
                GameEffect.GameOver -> {}
                null -> {}
            }
        }

        GutterTalkLaneScreen(
            modifier = modifier,
            onThrow = {
                dispatcher.invoke(it)
            },
            physicsSnapshot = state.physicsSnapshot,
            rolls = state.rolls,
            frame = state.currentFrame
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}