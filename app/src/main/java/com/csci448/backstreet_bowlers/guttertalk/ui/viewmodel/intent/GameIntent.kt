package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent

sealed class GameIntent : GutterTalkIntent() {
    data class ThrowBall(
        val swipeVelocityX: Double,
        val swipeVelocityZ: Double,
        val swipeSpin: Double
    ) : GameIntent()
    data object NewGame : GameIntent()

    /**
     * @param pinsHit A set of the pins that were hit. The set will be null if
     * the ball fell in the gutter. An empty set means the ball missed the pins,
     * but didn't fall in the gutter.
     */
    data class BallSettled(val pinsHit: Set<Int>?) : GameIntent()
}