package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.data.UserInformation
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.GutterTalkSingleRow
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.ScoresViewModel
import java.util.Date
import java.util.UUID

@Composable
fun UserScoresComposable(
    userScores: List<BowlingScore>,
    userStats: UserInformation?

) {
    val nameModifiers = listOf(
        R.string.scores_screen_rank_1,
        R.string.scores_screen_rank_2,
        R.string.scores_screen_rank_3
    )
    Column(
        modifier = Modifier
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Introductory "explanation text"
        Text(
            text = "Here are your bowling scores!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "The first scorecard is your most recent game, with the next cards ranked on top score",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Scorecards",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Column(
            modifier = Modifier
//                .fillMaxSize()
                .padding(8.dp)
                .weight(.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Check if user scores.values has anything inside it
            if (userStats == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Loading Player Scores...")
                }
            } else {
                var scoresList = mutableListOf<BowlingScore>()
                // Simulated logic of pulling scores
                if (!userScores.isEmpty()) {
                    userScores.forEach { scoreCard ->
                        scoresList.add(scoreCard)
                    }
                }
                scoresList.sortByDescending { it.datePlayed }

                if (!scoresList.isEmpty()) {
                    // Headers for the table
                    GutterTalkSingleRow(
                        BowlingScore(id = UUID.randomUUID().toString()),
                        R.string.scores_screen_header_1,
                        Modifier,
                        R.string.scores_screen_header_2
                    )

                    // Display most recent game
                    GutterTalkSingleRow(
                        scoresList.get(0),
                        R.string.scores_screen_recent_game,
                        Modifier
                    )

                    scoresList.sortByDescending { it.scores.get(9) } // Sorts by the largest score
                    scoresList.take(3).forEachIndexed { index, scoreCard ->
                        GutterTalkSingleRow(
                            scoreCard = scoreCard,
                            rankTag = nameModifiers.get(index),
                            modifier = Modifier
                        )
                    }
                } else {
                    Text(
                        text = "You have not played any games yet!  Return to the homescreen to get started",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            UserScoresStatistics(userStats)
        }
    }
}



@Composable
fun UserScoresStatistics(
    userStats: UserInformation?
) {
    // Goal is to get user statistics from here and display them nicely
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Lifetime Statistics",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (userStats == null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text("Loading Player Stats...")
            }
        } else {


            // Contain Games Played, Total Score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                // Games Played Card
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Games Played",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = userStats.gamesPlayed.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                // Total Score card
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Score",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = userStats.lifetimeScore.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                // Lifetime Spares card
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lifetime Spares",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = userStats.lifetimeSpares.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                // Lifetime Strikes card
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lifetime Strikes",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = userStats.lifetimeStrikes.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun PreviewUserScoresScreen(){
    UserScoresComposable(MockData.listOfScores, MockData.userInfo)
}

@Preview (showBackground = true)
@Composable
fun PreviewUserScoresScreenError(){
    UserScoresComposable(MockData.listOfScores, null)
}

private object MockData {
    val perfectGame = BowlingScore(
        id = "score_perfect_001",
        PlayerID = "SamMantle", // Using your identifier for consistency
        rolls = listOf(10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 10, 10),
        scores = listOf(30, 60, 90, 120, 150, 180, 210, 240, 270, 300),
        frameNumber = 10,
        score = 300,
        datePlayed = Date(1714564800000L)
    )

    val lowScoreGame = BowlingScore(
        id = "score_low_004",
        PlayerID = "SamMantle",
        rolls = listOf(1, 1, 2, 2, 0, 0, 3, 3, 0, 0, 4, 4, 0, 0, 5, 0, 0, 1, 2, 2, null),
        scores = listOf(2, 6, 6, 12, 12, 20, 20, 25, 26, 30),
        frameNumber = 10,
        score = 30,
        datePlayed = Date(1714651200000L)
    )

    val spareGame = BowlingScore(
        id = "score_spare_002",
        PlayerID = "SamMantle",
        rolls = listOf(9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9),
        scores = listOf(19, 38, 57, 76, 95, 114, 133, 152, 171, 190),
        frameNumber = 10,
        score = 190,
        datePlayed = Date(1714737600000L)
    )

    val mixedGame = BowlingScore(
        id = "score_mixed_003",
        PlayerID = "SamMantle",
        rolls = listOf(10, 0, 7, 3, 0, 5, 10, 0, 8, 1, 6, 2, 10, 0, 10, 0, 9, 0, 7, 3, 10),
        scores = listOf(20, 30, 35, 53, 62, 70, 90, 109, 118, 138),
        frameNumber = 10,
        score = 138,
        datePlayed = Date(1714824000000L)
    )

    val listOfScores = listOf(mixedGame, spareGame, perfectGame, lowScoreGame)

    val userInfo = UserInformation(
        PlayerID = "fake",
        username = "mantle",
        gamesPlayed = 5,
        lifetimeScore = 781,
        lifetimeSpares = 14,
        lifetimeStrikes = 6,
        location = "Denver",
        highestScore = 231
    )
}