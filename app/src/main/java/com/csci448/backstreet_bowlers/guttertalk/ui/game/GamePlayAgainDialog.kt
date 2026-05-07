package com.csci448.backstreet_bowlers.guttertalk.ui.game

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.csci448.backstreet_bowlers.guttertalk.R

@Composable
fun GamePlayAgainDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(text = stringResource(R.string.lane_dialog_again))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(R.string.lane_dialog_leave))
            }
        }
    )
}