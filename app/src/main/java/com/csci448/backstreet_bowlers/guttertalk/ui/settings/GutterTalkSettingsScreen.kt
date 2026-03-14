package com.csci448.backstreet_bowlers.guttertalk.ui.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GutterTalkSettingsScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "Settings Screen")
}

@Preview
@Composable
fun GutterTalkSettingsScreenPreview() {
    GutterTalkSettingsScreen()
}
