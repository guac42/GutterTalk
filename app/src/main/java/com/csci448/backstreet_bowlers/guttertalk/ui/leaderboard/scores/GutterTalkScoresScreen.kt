package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.GutterTalkScoreboard
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.ScoreCalculator
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.ScoresViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@Composable
fun GutterTalkScoresScreen(
    modifier: Modifier = Modifier,
    screen: Int = 0, // 0 maps to user scores, 1 maps to global, 2 maps to local
    userId: String? = FirebaseAuth.getInstance().currentUser?.uid,
    userLocation: String? = null,
    viewModel: ScoresViewModel = viewModel(factory = GutterTalkViewModelFactory())
) {
    // Temporary score for most recent game
//    var rolls: List<Int?> = listOf(10, 0, 10, 0, 7, 3, 7, 2, 4, 5, 9, 1, 0, 10, 10, 0, 10, 0, 5, 5, 8)
//    var scoreCard = BowlingScore(id= UUID.randomUUID(), rolls = rolls)
//    var scores: List<Int?> = listOf()
//    for(i in 1..10){
//        scores += (ScoreCalculator(scoreCard, i))
//    }
//    var scoreCard2 = BowlingScore(id= UUID.randomUUID(), rolls = rolls, scores = scores)

    // LaunchEffect for what we should load
    LaunchedEffect(screen, userId, userLocation) {
        when (screen){
            0 -> userId?.let { viewModel.loadUserScores(it) }
            1 -> viewModel.loadGlobalTopScores()
            2 -> userLocation?.let { viewModel.loadLocalTopScores(it) }
        }
    }

    val userScores = viewModel.userScores.collectAsState()
    val topUsers = viewModel.topUsers.collectAsState()

    // Temporary score for most recent game

    // Case: User scoreboard
    if(screen == 0) {
        Column(
            modifier = modifier
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//         Title Card
                // Headers for the table
                Text(text = "hello")
                GutterTalkSingleRow(
                    BowlingScore(id = UUID.randomUUID().toString()),
                    R.string.scores_screen_header_1,
                    modifier,
                    R.string.scores_screen_header_2
                )
                Log.d("GutterTalkScoresScreen", "Attempting to pull user scores")
                userScores.value.forEach { scoreCard ->
                    GutterTalkSingleRow(scoreCard, R.string.scores_screen_rank_1, modifier)
                    Log.d("GutterTalkScoresScreen", "Loaded user score ${scoreCard.id}")
                }

                // TODO: Use string resources for rank/recent game
//                GutterTalkSingleRow(scoreCard2, R.string.scores_screen_recent_game, modifier)
//                GutterTalkSingleRow(scoreCard2, R.string.scores_screen_rank_1, modifier)
//                GutterTalkSingleRow(
//                    BowlingScore(id = UUID.randomUUID()),
//                    R.string.scores_screen_rank_2,
//                    modifier
//                )
//                GutterTalkSingleRow(
//                    BowlingScore(id = UUID.randomUUID()),
//                    R.string.scores_screen_rank_3,
//                    modifier
//                )
            }
        }
    } else if (screen == 1) {
        // Case: Global scoreboard
        Column(
            modifier = modifier
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GutterTalkGlobalRow(R.string.scores_screen_global_username, R.string.scores_screen_global_stats, Modifier)
            }
        }
    } else {
        // Case: Local scoreboard

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
                modifier=Modifier
                    .weight(0.17f)
                    .padding(2.dp),
                shape = RectangleShape,
                border = BorderStroke(1.dp, Color.White),

                ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
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

@Composable
fun GutterTalkGlobalRow(
    rowOne: Int,
    rowTwo: Int,
    modifier: Modifier = Modifier,
    topScore: String? = null,
    lifetimeStrikes: String? = null,
    lifetimeGames: String? = null
){
    // not the header
        OutlinedCard(
            border = BorderStroke(3.dp, Color.Black),
            shape = RectangleShape
        ) {
            Row(
                modifier = modifier//.padding(1.dp)
                    .height(IntrinsicSize.Max)
            ) {
                Card(
                    modifier = Modifier
                        .weight(0.30f)
                        .padding(2.dp),
                    shape = RectangleShape,
                    border = BorderStroke(1.dp, Color.White),
                    ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        contentAlignment = Alignment.Center // Centers children both vertically and horizontally
                    ) {
                        Text(
                            text = stringResource(rowOne)
                        )
                    }
                }
                if(topScore == null) {
                    OutlinedCard(
                        modifier = Modifier
                            .weight(0.70f)
                            .padding(horizontal = 5.dp),
                        shape = RectangleShape,
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center // Centers children both vertically and horizontally
                        ) {
                            Text(
                                text = stringResource(rowTwo)
                            )
                        }
                    }
                } else {
                    // Fills in the rest of the stats
                    com.google.firebase.auth.FirebaseAuth.getInstance()
                }
            }
        }
}

@Composable
fun GutterTalkBuildPreviewUserScores(){
    var screen = 0
    var userId = "abc"
    var userLocation = "Denver"
    var modifier = Modifier

    var nameModifiers = listOf(R.string.scores_screen_rank_1, R.string.scores_screen_rank_2, R.string.scores_screen_rank_3)

    // Temporary score for most recent game

    // Case: User scoreboard
    if(screen == 0) {
        Column(
            modifier = modifier
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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(.7f)
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Check if user scores.values has anything inside it

                var scoresList = mutableListOf<BowlingScore>()
                // Simulated logic of pulling scores
                MockData.listOfScores.forEach { scoreCard ->
                    scoresList.add(scoreCard)
                }
                scoresList.sortByDescending {it.datePlayed}

                if(!scoresList.isEmpty()){
                    // Headers for the table
                    GutterTalkSingleRow(
                        BowlingScore(id = UUID.randomUUID().toString()),
                        R.string.scores_screen_header_1,
                        modifier,
                        R.string.scores_screen_header_2
                    )

                    // Display most recent game
                    GutterTalkSingleRow(
                        scoresList.get(0),
                        R.string.scores_screen_recent_game,
                        modifier
                    )

                    scoresList.sortByDescending { it.scores.get(10) } // Sorts by the largest score
                    scoresList.take(3).forEachIndexed { index, scoreCard ->
                        GutterTalkSingleRow(
                            scoreCard = scoreCard,
                            rankTag = nameModifiers.get(index),
                            modifier = modifier
                        )
                    }
                }


                /* Replace above logic inside of here for real screen
                userScores.value.forEach { scoreCard ->
                    GutterTalkSingleRow(scoreCard, R.string.scores_screen_rank_1, modifier)
                    Log.d("GutterTalkScoresScreen", "Loaded user score ${scoreCard.id}")
                }*/



                // TODO: Use string resources for rank/recent game
//                GutterTalkSingleRow(scoreCard2, R.string.scores_screen_recent_game, modifier)
//                GutterTalkSingleRow(scoreCard2, R.string.scores_screen_rank_1, modifier)
//                GutterTalkSingleRow(
//                    BowlingScore(id = UUID.randomUUID()),
//                    R.string.scores_screen_rank_2,
//                    modifier
//                )
//                GutterTalkSingleRow(
//                    BowlingScore(id = UUID.randomUUID()),
//                    R.string.scores_screen_rank_3,
//                    modifier
//                )
            }
        }
    } else if (screen == 1) {
        // Case: Global scoreboard
        Column(
            modifier = modifier
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GutterTalkGlobalRow(R.string.scores_screen_global_username, R.string.scores_screen_global_stats, Modifier)
            }
        }
    } else {
        // Case: Local scoreboard

    }
}


@Preview (showBackground = true)
@Composable
fun GutterTalkScoresGlobalPreview() {
//    GutterTalkScoresScreen(screen = 1)
}


@Preview (showBackground = true)
@Composable
fun GutterTalkScoresScreenPreview() {
    GutterTalkBuildPreviewUserScores()
}

object MockData {
    val perfectGame = BowlingScore(
        id = "score_perfect_001",
        PlayerID = "SamMantle", // Using your identifier for consistency
        rolls = listOf(10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 10, 10),
        scores = listOf(30, 60, 90, 120, 150, 180, 210, 240, 270, 300),
        frameNumber = 10,
        score = 300
    )

    val lowScoreGame = BowlingScore(
        id = "score_low_004",
        PlayerID = "SamMantle",
        rolls = listOf(1, 1, 2, 2, 0, 0, 3, 3, 0, 0, 4, 4, 0, 0, 5, 0, 0, 1, 2, 2, null),
        scores = listOf(2, 6, 6, 12, 12, 20, 20, 25, 26, 30),
        frameNumber = 10,
        score = 30
    )

    val spareGame = BowlingScore(
        id = "score_spare_002",
        PlayerID = "SamMantle",
        rolls = listOf(9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9, 1, 9),
        scores = listOf(19, 38, 57, 76, 95, 114, 133, 152, 171, 190),
        frameNumber = 10,
        score = 190
    )

    val mixedGame = BowlingScore(
        id = "score_mixed_003",
        PlayerID = "SamMantle",
        rolls = listOf(10, 0, 7, 3, 0, 5, 10, 0, 8, 1, 6, 2, 10, 0, 10, 0, 9, 0, 7, 3, 10),
        scores = listOf(20, 30, 35, 53, 62, 70, 90, 109, 118, 138),
        frameNumber = 10,
        score = 138
    )

    val listOfScores = listOf(mixedGame, spareGame, perfectGame, lowScoreGame)
}
