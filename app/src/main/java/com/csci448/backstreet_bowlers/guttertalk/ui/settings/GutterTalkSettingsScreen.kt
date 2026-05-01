package com.csci448.backstreet_bowlers.guttertalk.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.common.GutterTalkButton
import androidx.compose.runtime.setValue






@Composable
fun GutterTalkSettingsScreen(
    modifier: Modifier = Modifier,
    isMusicOn: Boolean,
    musicVolume: Float,
    onToggleMusicClick: (Boolean) -> Unit,
    onMusicVolumeChange: (Float) -> Unit,
    onToggleInsultClick: (Boolean) -> Unit
) {
        var showMusicDialog by remember { mutableStateOf(false) }
        if (showMusicDialog) {
            AlertDialog(
                onDismissRequest = { showMusicDialog = false },
                title = { Text("Music") },
                text = { Text("Turn music on or off?") },
                confirmButton = {
                    TextButton(onClick = {
                        onToggleMusicClick(true)
                        showMusicDialog = false
                    }) { Text("Music On") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        onToggleMusicClick(false)
                        showMusicDialog = false
                    }) { Text("Music Off") }
                }
            )
        }
    var showInsultsDialog by remember { mutableStateOf(false) }
    if (showInsultsDialog) {
        AlertDialog(
            onDismissRequest = { showInsultsDialog = false },
            title = { Text("Insults") },
            text = { Text("Turn insults on or off?") },
            confirmButton = {
                TextButton(onClick = {
                    onToggleInsultClick(true)
                    showInsultsDialog = false
                }) { Text("Insults On") }
            },
            dismissButton = {
                TextButton(onClick = {
                    onToggleInsultClick(false)
                    showInsultsDialog = false
                }) { Text("Insults Off") }
            }
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        GutterTalkButton(
            text = stringResource(R.string.settings_toggle_music_button),
            onClick = { showMusicDialog = true }
        )
        Slider(
            value = musicVolume,
            onValueChange = onMusicVolumeChange,
            valueRange = 0f..1f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        GutterTalkButton(
            text = stringResource(R.string.settings_toggle_insults_button),
            onClick = {showInsultsDialog = true}
        )

    }
}

@Preview
@Composable
fun GutterTalkSettingsScreenPreview() {
    GutterTalkSettingsScreen(
        modifier = Modifier,
        isMusicOn = false,
        musicVolume = 0.5f,
        onToggleMusicClick = {},
        onMusicVolumeChange = {},
        onToggleInsultClick = {_ ->}
    )
}
