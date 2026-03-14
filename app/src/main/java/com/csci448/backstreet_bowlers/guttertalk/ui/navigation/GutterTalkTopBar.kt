package com.csci448.backstreet_bowlers.guttertalk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs.IScreenSpec

@Composable
fun GutterTalkTopBar(navController: NavHostController) {
    val navBackStackEntryState = navController.currentBackStackEntryAsState()
    IScreenSpec.TopBar(navController, navBackStackEntryState.value)
}
