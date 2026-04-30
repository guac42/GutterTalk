package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent

sealed class GameIntent : GutterTalkIntent() {
    data object ThrowBall : GameIntent()
    data object ResetPins : GameIntent()

    /**
     * @param pinsHit A set of the pins that were hit. The set will be null if
     * the ball fell in the gutter. An empty set means the ball missed the pins,
     * but didn't fall in the gutter.
     */
    data class BallSettled(val pinsHit: Set<Int>?) : GameIntent()
}