package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state

import kotlinx.serialization.Serializable

@Serializable
data class LoginState(
    val loading: Boolean = false,
    val username: String = "",
    val password: String = ""
) : GutterTalkState