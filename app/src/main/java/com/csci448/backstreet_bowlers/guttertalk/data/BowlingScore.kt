package com.csci448.backstreet_bowlers.guttertalk.data

import java.util.UUID

/**
 * BowlingScore
 * This holds all the data for a single scorecard for a bowling game
 * rolls: holds the scores for each role in the game
 *      1-9 have 2 rolls, with 10 having 3
 * Scores: holds the scores for each frame of the game
 * FrameNumber: The frame number that the bowler is on, if the value is null the game has finished
 */
data class BowlingScore (
    val id: UUID,
    val rolls: List<Int?> = List(21){null}, // Will need to use a type converter for this, 21 possible entries
    val scores: List<Int?> = List(10){null}, // Will need to use a type converter for this, 10 possible entries
    val frameNumber: Int? = 1
)