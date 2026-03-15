package com.csci448.backstreet_bowlers.guttertalk.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.csci448.backstreet_bowlers.guttertalk.ui.common.GutterTalkButton
import  com.csci448.backstreet_bowlers.guttertalk.R

@Composable
fun GutterTalkSettingsScreen(
    modifier: Modifier = Modifier,
    onToggleMusicClick: () -> Unit,
    onMusicSliderClick: () -> Unit,
    onToggleInsultClick: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GutterTalkButton(
            text = stringResource(R.string.settings_toggle_music_button),
            onClick = onToggleMusicClick

        )
        GutterTalkButton(
            text = stringResource(R.string.settings_music_slider_button),
            onClick = onMusicSliderClick
        )
        GutterTalkButton(
            text = stringResource(R.string.settings_toggle_insults_button),
            onClick = onToggleInsultClick
        )
    }

}

@Preview
@Composable
fun GutterTalkSettingsScreenPreview() {
    GutterTalkSettingsScreen(
        modifier = Modifier,
        onToggleMusicClick = {},
        onMusicSliderClick = {},
        onToggleInsultClick = {}
    )
}
