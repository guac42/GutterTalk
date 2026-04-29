package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.settings.GutterTalkSettingsScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.SettingsViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.SettingsIntent

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
        val context = LocalContext.current
        val viewModel: SettingsViewModel = viewModel(
            viewModelStoreOwner = navBackStackEntry,
            factory = GutterTalkViewModelFactory(),
            extras = GutterTalkViewModelFactory.creationExtras(
                navBackStackEntry.defaultViewModelCreationExtras,
                context
            )
        )
        val state by viewModel.stateFlow.collectAsState()

        GutterTalkSettingsScreen(
            modifier = modifier,
            isMusicOn = state.isMusicOn,
            musicVolume = state.musicVolume,
            onToggleMusicClick = { enabled ->
                viewModel.handleIntent(SettingsIntent.SetMusicEnabled(enabled))
            },
            onMusicVolumeChange = { volume ->
                viewModel.handleIntent(SettingsIntent.SetMusicVolume(volume))
            },
            onToggleInsultClick = {}
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}