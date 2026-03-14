package com.csci448.backstreet_bowlers.guttertalk.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GutterTalkMainScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "Main Screen")
}

@Preview
@Composable
fun GutterTalkMainScreenPreview() {
    GutterTalkMainScreen()
}
