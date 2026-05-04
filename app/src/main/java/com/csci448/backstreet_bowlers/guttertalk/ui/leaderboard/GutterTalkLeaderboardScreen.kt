package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.common.GutterTalkButton

@Composable
fun GutterTalkLeaderboardScreen(
    modifier: Modifier = Modifier,
    isLocationEnabled: Boolean,
    onUserScoresClick: () -> Unit,
    onGlobalLeaderboardClick: () -> Unit,
    onLocalLeaderboardClick: () -> Unit
) {
    Log.d("448.GutterTalkLeaderboardScreen", "Current enabled value is: $isLocationEnabled")
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GutterTalkButton(
            text = stringResource(R.string.leaderboard_screen_scores),
            onClick = onUserScoresClick
        )
        GutterTalkButton(
            text = stringResource(R.string.leaderboard_screen_global),
            onClick = onGlobalLeaderboardClick,
            enabled = true
        )
        GutterTalkButton(
            text = stringResource(R.string.leaderboard_screen_local),
            onClick = onLocalLeaderboardClick,
            enabled = isLocationEnabled
        )
    }
}

@Preview
@Composable
fun GutterTalkLeaderboardScreenPreview() {
    GutterTalkLeaderboardScreen(
        onUserScoresClick = {},
        onGlobalLeaderboardClick = {},
        onLocalLeaderboardClick = {},
        isLocationEnabled = true
    )
}