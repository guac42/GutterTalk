package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent

sealed class  SettingsIntent : GutterTalkIntent() {
    class SetMusicEnabled(val enabled: Boolean) : SettingsIntent()
    class SetMusicVolume(val volume: Float) : SettingsIntent()
    class SetInsultsEnabled(val enabled: Boolean) : SettingsIntent()
}

