package com.csci448.backstreet_bowlers.guttertalk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs.IScreenSpec

@Composable
fun GutterTalkNavHost(
    modifier: Modifier = Modifier.Companion,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = IScreenSpec.Companion.ROOT,
    ) {
        navigation(
            route = IScreenSpec.Companion.ROOT,
            startDestination = IScreenSpec.Companion.startDestination
        ) {
            IScreenSpec.Companion.allScreens.forEach { (_, screen) ->
                if (screen != null) {
                    composable(
                        route = screen.route,
                        arguments = screen.arguments
                    ) { navBackStackEntry ->
                        screen.Content(
                            modifier = Modifier.Companion,
                            navController = navController,
                            navBackStackEntry = navBackStackEntry
                        )
                    }
                }
            }
        }
    }
}