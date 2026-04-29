package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state

import kotlinx.serialization.Serializable

@Serializable
data class SettingsState(
    val isMusicOn: Boolean = false,
    val musicVolume: Float = 0.5f
) : GutterTalkState
