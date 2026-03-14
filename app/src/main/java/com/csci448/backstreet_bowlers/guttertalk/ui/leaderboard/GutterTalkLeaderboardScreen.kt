package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GutterTalkLeaderboardScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "Leaderboard Screen")
}

@Preview
@Composable
fun GutterTalkLeaderboardScreenPreview() {
    GutterTalkLeaderboardScreen()
}
