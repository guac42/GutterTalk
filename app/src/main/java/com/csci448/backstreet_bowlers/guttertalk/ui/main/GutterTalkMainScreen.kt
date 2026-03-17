package com.csci448.backstreet_bowlers.guttertalk.ui.main

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
fun GutterTalkMainScreen(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onLeaderBoardClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GutterTalkButton(
            text = stringResource(R.string.main_play_button),
            onClick = onPlayClick
        )
        GutterTalkButton(
            text = stringResource(R.string.main_leaderboard_button),
            onClick = onLeaderBoardClick
        )
        GutterTalkButton(
            text = stringResource(R.string.main_settings_button),
            onClick = onSettingsClick
        )
    }
}

@Preview
@Composable
fun GutterTalkMainScreenPreview() {
    GutterTalkMainScreen(
        onPlayClick = {},
        onLeaderBoardClick = {},
        onSettingsClick = {}
    )
}
