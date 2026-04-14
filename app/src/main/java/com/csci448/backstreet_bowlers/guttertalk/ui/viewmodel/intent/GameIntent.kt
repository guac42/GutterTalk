package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent

sealed class GameIntent : GutterTalkIntent() {
    data class AimBall(val delta: Float) : GameIntent()
    data class SetPower(val power: Float) : GameIntent()
    data object ThrowBall : GameIntent()
    data object ResetPins : GameIntent()
    data class BallSettled(val pinsHit: Set<Int>) : GameIntent()
}