package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect

sealed class GameEffect : GutterTalkEffect() {
    /** Emitted when an insult is made */
    data class Insult(val insult: String) : GameEffect()
}