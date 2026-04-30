package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state

import kotlinx.serialization.Serializable

/**
 * @param knockedPins  Bitmask of knocked-down pins (bit i = pin i is down).
 *                     0 = all standing; 0b1111111111 = all ten knocked.
 * @param currentFrame 1-indexed bowling frame (1..10).
 * @param throwInFrame 0 = first ball of frame, 1 = second ball.
 */
@Serializable
data class GameState(
    val ballInMotion: Boolean    = false,
    val knockedPins: Int         = 0,
    val frameScores: List<Int>   = emptyList(),
    val currentFrame: Int        = 1,
    val throwInFrame: Int        = 0,
) : GutterTalkState