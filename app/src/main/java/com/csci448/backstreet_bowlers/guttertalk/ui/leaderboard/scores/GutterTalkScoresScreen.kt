package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.GutterTalkScoreboard
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.ScoreCalculator
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.UserScoresComposable
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.GutterTalkViewModelFactory
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.ScoresViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import java.util.UUID
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.csci448.backstreet_bowlers.guttertalk.data.UserInformation

@Composable
fun GutterTalkScoresScreen(
    modifier: Modifier = Modifier,
    screen: Int = 0, // 0 maps to user scores, 1 maps to global, 2 maps to local
    userId: String? = FirebaseAuth.getInstance().currentUser?.uid,
    userLocation: String? = null,
    viewModel: ScoresViewModel = viewModel(factory = GutterTalkViewModelFactory())
) {
    // LaunchEffect for what we should load
    LaunchedEffect(screen, userId, userLocation) {
        when (screen){
            0 -> userId?.let {
                viewModel.loadUserScores(it)
                viewModel.loadUserInformation(it)
            }
            1 -> viewModel.loadGlobalTopScores()
            2 -> userLocation?.let { viewModel.loadLocalTopScores(it) }
        }
    }

    val userScores by viewModel.userScores.collectAsState()
    val topUsers = viewModel.topUsers.collectAsState()
    val userStats by viewModel.userStats.collectAsState()

    // Case: User scoreboard
    if(screen == 0) {
        // Send userStats
        UserScoresComposable(userScores = userScores, userStats = userStats)
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
                }
            }
        }
}


