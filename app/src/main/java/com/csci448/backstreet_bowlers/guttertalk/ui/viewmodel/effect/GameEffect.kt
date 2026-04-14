package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect

sealed class GameEffect : GutterTalkEffect() {
    /** Emitted when a strike is detected so the UI can celebrate. */
    data object Strike : GameEffect()

    /** Emitted when a spare is detected. */
    data object Spare : GameEffect()
}