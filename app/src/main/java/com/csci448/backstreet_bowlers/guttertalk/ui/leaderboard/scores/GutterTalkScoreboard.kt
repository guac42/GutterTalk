package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.data.database.BowlingScore
import java.util.UUID

/**
 * Scoreboard Screen
 * This composable is meant to create the foundations for any scoreboard used in the app
 * @param modifier to be applied to the composable
 * @param scoreCard has all the data about a specific game (BowlingScore)
 */
@Composable
fun GutterTalkScoreboard(
    modifier: Modifier = Modifier,
    scoreCard: BowlingScore
) {
    Row(modifier = modifier.padding(1.dp).height(IntrinsicSize.Max)){
        if(scoreCard.frameNumber != null) {
                for (frame in 1..9) {
                    var index = (frame-1)*2
                    SingleFrame(
                        modifier = modifier.weight(1f),
                        score1 = scoreCard.rolls[index],
                        score2 = scoreCard.rolls[index + 1],
                        score = scoreCard.scores[frame-1]
                    )
                }
                SingleFrame(
                    modifier = modifier.weight(1f),
                    score1 = scoreCard.rolls[18],
                    score2 = scoreCard.rolls[19],
                    score3 = scoreCard.rolls[20],
                    score = scoreCard.scores[9],
                    frameTen = true
                )
        }
    }
}

@Composable
fun SingleFrame(modifier: Modifier, score1: Int?, score2: Int?, score:Int?, score3:Int? = null, frameTen:Boolean = false){
    // Handles all the logic for "special scores"
    var score1String:String = " "
    var score2String:String = " "
    var score3String:String = " "
    var runningScore:String = " "
    if(score != null) runningScore = score.toString()
    if(frameTen && score1 != null){
        if (score1 == 10){
            score1String = "X"
        }else {
            score1String = score1.toString()
        }
        if (score2 != null) {
            if (score1 + score2 == 10) {
                score2String = "/"
            } else if (score2 == 10) {
                score2String = "X"
            } else {
                score2String = score2.toString()
            }
            if (score3 != null) {
                if (score3 == 10) {
                    score3String = "X"
                } else if (score2 + score3 == 10) {
                    score3String = "/"
                } else {
                    score3String = score3.toString()
                }
            }
        }
    }else
    if(score1 != null){
        if (score1 == 10){
            score2String = "X"
        }
        else{
            score1String = score1.toString()
            if(score2 != null){
                if(score1 + score2 == 10){
                    score2String = "/"
                }
                else{
                    score2String = score2.toString()
                }
            }
        }
    }

    OutlinedCard(
//        modifier = modifier.size(35.dp), //TODO: Remove this enforced sizing
        modifier = modifier,
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.Black)
    ){
        Column() {
            // Top Row
            Row(){
                // Score 1
                OutlinedCard(
                    modifier = Modifier.weight(.5f),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RectangleShape // Could be removed (the default is a circle) to indicate a split
                ){
                    Text(text = score1String, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                // Score 2
                OutlinedCard(
                    modifier = Modifier.weight(.5f),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RectangleShape
                ){
                    Text(text = score2String, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                // Score 3
                if(frameTen){
                    OutlinedCard(
                        modifier = Modifier.weight(.5f),
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RectangleShape
                    ){
                        Text(text = score3String, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                }
            }
            OutlinedCard(
                modifier = Modifier.align(Alignment.End),
                border = BorderStroke(1.dp, Color.White),
                shape = RectangleShape
            ){
                Text(text = runningScore, modifier = Modifier.padding(2.dp))
            }
        }
    }
}

@Preview
@Composable
fun EmptyGutterTalkScoreboardScreenPreview() {
    var scoreCard = BowlingScore(id= UUID.randomUUID().toString())
    GutterTalkScoreboard(scoreCard = scoreCard)
}

@Preview
@Composable
fun GutterTalkScoreboardScreenPreview() {
    var rolls: List<Int?> = listOf(10, 0, 10, 0, 7, 3, 7, 2, 4, 5, 9, 1, 0, 10, 10, 0, 10, 0, 5, 5, 8)
    var scoreCard = BowlingScore(id= UUID.randomUUID().toString(), rolls = rolls)
    var scores: List<Int?> = listOf()
    for(i in 1..10){
        scores += (ScoreCalculator(scoreCard, i))
    }
    var scoreCard2 = BowlingScore(id= UUID.randomUUID().toString(), rolls = rolls, scores = scores)
    GutterTalkScoreboard(scoreCard = scoreCard2)
}

@Preview
@Composable
fun GutterTalkScoreboardScreenInProgressPreview() {
    var rolls: List<Int?> = listOf(10, 0, 10, 0, 7, 3, 7, 2, 4, 5, 9, 1, 0, 10, 10, 0, 10, null, null, null, null)
    var scoreCard = BowlingScore(id= UUID.randomUUID().toString(), rolls = rolls)
    var scores: List<Int?> = listOf()
    for(i in 1..10){
        scores += (ScoreCalculator(scoreCard, i))
    }
    var scoreCard2 = BowlingScore(id= UUID.randomUUID().toString(), rolls = rolls, scores = scores)
    GutterTalkScoreboard(scoreCard = scoreCard2)
}

@Preview
@Composable
fun SingleFramePreview(){
    SingleFrame(score1 = 10, score2 = 5, modifier = Modifier, score = 20)
}

@Preview
@Composable
fun FrameTenPreview(){
    SingleFrame(score1 = 10, score2 = 5, score3 = 5, modifier = Modifier, score = null, frameTen = true)
}
