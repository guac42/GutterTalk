package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.GutterTalkScoreboard
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.ScoreCalculator
import java.util.UUID

@Composable
fun GutterTalkScoresScreen(
    modifier: Modifier = Modifier
    // Get top scores and the most recent score here.
) {
    var rolls: List<Int?> = listOf(10, 0, 10, 0, 7, 3, 7, 2, 4, 5, 9, 1, 0, 10, 10, 0, 10, 0, 5, 5, 8)
    var scoreCard = BowlingScore(id= UUID.randomUUID(), rolls = rolls)
    var scores: List<Int?> = listOf()
    for(i in 1..10){
        scores += (ScoreCalculator(scoreCard, i))
    }
    var scoreCard2 = BowlingScore(id= UUID.randomUUID(), rolls = rolls, scores = scores)
    // Probably wont have the scores for now
    Column(
        modifier = modifier
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Box(
            modifier = Modifier.padding(2.dp).weight(.3f),
            contentAlignment = Alignment.Center // Centers children both vertically and horizontally
        ) {
            Text(
                text= stringResource(R.string.scores_screen_title),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp).weight(.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
//         Title Card
            // Headers for the table
            GutterTalkSingleRow(BowlingScore(id=UUID.randomUUID()), R.string.scores_screen_header_1, modifier, R.string.scores_screen_header_2)
            // TODO: Use string resources for rank/recent game
            GutterTalkSingleRow(scoreCard2, R.string.scores_screen_recent_game, modifier)
            GutterTalkSingleRow(scoreCard2, R.string.scores_screen_rank_1, modifier)
            GutterTalkSingleRow(BowlingScore(id=UUID.randomUUID()), R.string.scores_screen_rank_2, modifier)
            GutterTalkSingleRow(BowlingScore(id=UUID.randomUUID()), R.string.scores_screen_rank_3, modifier)

        }
    }
}

@Composable
fun GutterTalkSingleRow(scoreCard: BowlingScore, rankTag:Int, modifier: Modifier, scoreTag:Int? = null,){
    OutlinedCard(
        border = BorderStroke(3.dp, Color.Black),
        shape = RectangleShape
    ) {
        Row(
            modifier = modifier//.padding(1.dp)
                .height(IntrinsicSize.Max)
        ){
            Card(
                modifier=Modifier.weight(0.15f).padding(2.dp),
                shape = RectangleShape,
                border = BorderStroke(1.dp, Color.White),

                ){
                Box(
                    modifier = Modifier.fillMaxSize().padding(2.dp),
                    contentAlignment = Alignment.Center // Centers children both vertically and horizontally
                ) {
                    Text(
                        text=stringResource(rankTag)
                    )
                }
            }
            // Builds the header of the table
            if(scoreTag != null){
                OutlinedCard(
                    modifier=Modifier.weight(0.80f),
                    shape = RectangleShape,
                    border = BorderStroke(1.dp, Color.Black),
                ){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // Centers children both vertically and horizontally
                    ) {
                        Text(
                            text=stringResource(scoreTag)
                        )
                    }
                }
            }else{
                // Fills in the scorecard is the header is not null
                Card(
                    modifier=Modifier.weight(0.80f),
                    shape = RectangleShape
                ){
                    GutterTalkScoreboard(scoreCard = scoreCard)
                }
            }
        }
    }
}

@Preview
@Composable
fun GutterTalkScoresScreenPreview() {
    GutterTalkScoresScreen()
}
