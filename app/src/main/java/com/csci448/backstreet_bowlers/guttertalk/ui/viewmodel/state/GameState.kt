package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state

import com.csci448.backstreet_bowlers.guttertalk.util.PhysicsSnapshot3D
import kotlinx.serialization.Serializable

/**
 * @param knockedPins  Number of pins knocked down
 * @param currentFrame 1-indexed bowling frame (1..10).
 * @param throwInFrame 0 = first ball of frame, 1 = second ball.
 */
@Serializable
data class GameState(
    val ballInMotion: Boolean    = false,
    val knockedPins: Int         = 0,
    val rolls: List<Int>   = emptyList(),
    val currentFrame: Int        = 1,
    val throwInFrame: Int        = 0,
    val physicsSnapshot: PhysicsSnapshot3D? = null,
) : GutterTalkState