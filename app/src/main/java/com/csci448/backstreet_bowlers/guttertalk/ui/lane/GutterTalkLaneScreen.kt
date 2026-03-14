package com.csci448.backstreet_bowlers.guttertalk.ui.lane

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GutterTalkLaneScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "Lane Screen")
}

@Preview
@Composable
fun GutterTalkLaneScreenPreview() {
    GutterTalkLaneScreen()
}
