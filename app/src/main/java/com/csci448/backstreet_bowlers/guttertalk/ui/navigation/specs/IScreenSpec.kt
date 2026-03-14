package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R

sealed interface IScreenSpec {
    companion object {
        private const val LOG_TAG = "448.IScreenSpec"

        val allScreens = IScreenSpec::class.sealedSubclasses.associate {
            Log.d(
                LOG_TAG,
                "allScreens: mapping route \"${it.objectInstance?.route ?: ""}\" to object \"${it.objectInstance}\""
            )
            it.objectInstance?.route to it.objectInstance
        }
        const val ROOT = "guttertalk"
        val startDestination = LoginScreenSpec.route

        @Composable
        fun TopBar(
            navController: NavHostController, navBackStackEntry: NavBackStackEntry?
        ) {
            val route = navBackStackEntry?.destination?.route ?: ""
            allScreens[route]?.TopAppBarContent(
                navController = navController, navBackStackEntry = navBackStackEntry
            )
        }
    }

    val route: String
    val arguments: List<NamedNavArgument>
    val title: Int
    fun buildRoute(vararg args: String?): String

    @Composable
    fun Content(
        modifier: Modifier, navController: NavHostController, navBackStackEntry: NavBackStackEntry
    )

    @Composable
    fun TopAppBarActions(
        navController: NavHostController, navBackStackEntry: NavBackStackEntry?
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopAppBarContent(
        navController: NavHostController, navBackStackEntry: NavBackStackEntry?
    ) {
        TopAppBar(
            navigationIcon = if (navController.previousBackStackEntry != null) {
                {
                    IconButton(
                        onClick = { navController.navigate(LoginScreenSpec.route) }) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = stringResource(id = R.string.menu_back_desc)
                        )
                    }
                }
            } else {
                { }
            },
            title = { Text(text = stringResource(id = title)) },
            actions = { TopAppBarActions(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            ) }
        )
    }
}