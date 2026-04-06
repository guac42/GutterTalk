package com.csci448.backstreet_bowlers.guttertalk.ui.navigation.specs

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.login.GutterTalkLoginScreen
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.LoginViewModel
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.collectInLaunchedEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.LoginEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.LoginIntent

data object LoginScreenSpec : IScreenSpec {
    private const val LOG_TAG = "448.LoginScreenSpec"

    override val route = "login"
    override val arguments: List<NamedNavArgument> = emptyList()
    override val title = R.string.login_screen_title
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
        )[LoginViewModel::class]

        val (state, dispatcher, effects) = viewModel.use(navBackStackEntry)

        effects.collectInLaunchedEffect {
            when(it) {
                LoginEffect.LoginSucceeded -> {
                    Log.d(LOG_TAG, "Handling login success")
                    dispatcher.invoke(LoginIntent.Clear)
                    navController.navigate(MainScreenSpec.route)
                }
                LoginEffect.LoginFailed -> {
                    Log.d(LOG_TAG, "Handling login failure")
                    val toast = Toast.makeText(context, R.string.login_failed, Toast.LENGTH_SHORT)
                    toast.show()
                }
                null -> {}
            }
        }

        GutterTalkLoginScreen(
            modifier = modifier,
            username = state.username,
            onNewUsername = { dispatcher.invoke( LoginIntent.NewUsernameString(it) ) },
            password = state.password,
            onNewPassword = { dispatcher.invoke( LoginIntent.NewPasswordString(it) ) },
            onLoginUser = {
                Log.d(LOG_TAG, "Login as user")
                if (state.username.isNotBlank() && state.password.isNotBlank()) {
                    dispatcher.invoke(LoginIntent.Login)
                } else {
                    val toast = Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                    toast.show()
                }
            },
            onLoginGuest = {
                Log.d(LOG_TAG, "Login as guest")
                navController.navigate(MainScreenSpec.route)
            },
            onCreateUser = {
                Log.d(LOG_TAG, "Create user")
                if(state.username.isNotBlank() && state.password.isNotBlank()){
                    dispatcher.invoke(LoginIntent.CreateUser)
                } else {
                    val toast = Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                }
            },
            loading = state.loading
        )
    }

    @Composable
    override fun TopAppBarActions(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry?
    ) { }
}