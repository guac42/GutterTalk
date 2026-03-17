package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.csci448.backstreet_bowlers.guttertalk.ui.common.GutterTalkButton
import com.csci448.backstreet_bowlers.guttertalk.R

@Composable
fun GutterTalkLeaderboardScreen(
    modifier: Modifier = Modifier,
    onUserScoresClick: () -> Unit,
    onGlobalLeaderboardClick: () -> Unit,
    onLocalLeaderboardClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GutterTalkButton(
            text = stringResource(R.string.leaderboard_screen_user_scores),
            onClick = onUserScoresClick
        )
        GutterTalkButton(
            text = stringResource(R.string.leaderboard_screen_global),
            onClick = onGlobalLeaderboardClick
        )

        GutterTalkButton(
            text = stringResource(R.string.leaderboard_screen_local),
            onClick = onLocalLeaderboardClick
        )
    }
}

@Preview
@Composable
fun GutterTalkLeaderboardScreenPreview() {
    GutterTalkLeaderboardScreen(
        onUserScoresClick = {},
        onGlobalLeaderboardClick = {},
        onLocalLeaderboardClick = {}
    )
}