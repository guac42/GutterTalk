package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores

import com.csci448.backstreet_bowlers.guttertalk.data.database.BowlingScore

/**
 * Likely an arbitrary class that we will remove later
 * Currently used to return the score for a given frame
 */

//fun ScoreCalculator (scoreCard: BowlingScore, frameNumber: Int): Int? {
//    fun getScore(frameNumber: Int): Int?{
//        return scoreCard.rolls[frameNumber]
//    }
//    var score:Int = 0
//    var roleIndex:Int = 0
//    for(frame in 1..10){
//        if(frame > frameNumber){
//            break
//        }
//        if(frame == frameNumber){
//            // Could def error out if frame numbers match but second role hasn't been taken
//            if(frame != 10){
//                if(getScore(roleIndex + 2) != null){
//                    continue
//                }else{
//                    return null
//                }
//            }
//            if(getScore(roleIndex + 1) != null) {
//                score += getScore(roleIndex)!! + getScore(roleIndex+1)!!
//            }
//            if(frame == 10 && getScore(roleIndex + 2) != null) {
//                score += getScore(roleIndex + 2)!!
//            }
//            return score
//        }
//
//        var currentScore = getScore(roleIndex)
//        if(currentScore == null){
//            return null
//        }
//        // Is it a strike?
//        if(currentScore == 10){
//            if(getScore(roleIndex + 2) != null){
//                score += 10
//                // Gets the next score
//                var role1:Int = getScore(roleIndex + 2)!!
//                if(frame != 9){
//                    if(role1 == 10 && getScore(roleIndex + 4) != null){
//                        score += getScore(roleIndex + 4)!!
//                    }else{
//                        score += getScore(roleIndex + 3)!!
//                    }
//                }else{
//                    score += getScore(roleIndex + 2)!! + getScore(roleIndex + 3)!!
//                }
//            }else{
//                return null
//            }
//            // Checking for spare
//        }else if(getScore(roleIndex+1) != null && currentScore + getScore(roleIndex+1)!! == 10){
//            score += currentScore + getScore(roleIndex+1)!!
//            if(getScore(roleIndex+2)!= null){
//                score += getScore(roleIndex+2)!!
//            }else{
//                return null
//            }
//            // Not a strike or spare
//        } else if(getScore(roleIndex+1) != null){
//            score += currentScore + getScore(roleIndex+1)!!
//        }
//        roleIndex += 2
//    }
//    return null
//}


// THANK GOD FOR GEMINI!!!!!!
// FUCK BOWLING THIS SHIT SUCKS
fun ScoreCalculator(scoreCard: BowlingScore, frameNumber: Int): Int? {
    val rolls = scoreCard.rolls
    var totalScore = 0

    for (frame in 1..frameNumber) {
        val rollIndex = (frame - 1) * 2
        val firstRoll = rolls.getOrNull(rollIndex) ?: return null

        if (frame < 10) {
            val secondRoll = rolls.getOrNull(rollIndex + 1) ?: return null

            // STRIKE
            if (firstRoll == 10) {
                // Look ahead for 2 bonus rolls, skipping placeholders
                val bonus1 = rolls.getOrNull(rollIndex + 2) ?: return null

                val bonus2 = if (bonus1 == 10 && frame < 9) {
                    // If next is strike, skip its placeholder (index + 3) and take index + 4
                    rolls.getOrNull(rollIndex + 4)
                } else {
                    // Otherwise, just take the next logical roll
                    rolls.getOrNull(rollIndex + 3)
                } ?: return null

                totalScore += 10 + bonus1 + bonus2
            }
            // SPARE
            else if (firstRoll + secondRoll == 10) {
                val bonus1 = rolls.getOrNull(rollIndex + 2) ?: return null
                totalScore += 10 + bonus1
            }
            // OPEN
            else {
                totalScore += firstRoll + secondRoll
            }
        } else {
            // FRAME 10: Indices 18, 19, 20
            val r1 = rolls.getOrNull(18) ?: return null
            val r2 = rolls.getOrNull(19) ?: return null

            // If it's a strike or spare, we need the 3rd ball
            if (r1 == 10 || r1 + r2 == 10) {
                val r3 = rolls.getOrNull(20) ?: return null
                totalScore += r1 + r2 + r3
            } else {
                totalScore += r1 + r2
            }
        }
    }
    return totalScore
}