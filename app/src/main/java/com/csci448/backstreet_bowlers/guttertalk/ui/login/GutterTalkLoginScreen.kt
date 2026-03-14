package com.csci448.backstreet_bowlers.guttertalk.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.common.GutterTalkButton

@Composable
fun GutterTalkLoginScreen(
    modifier: Modifier = Modifier,
    username: String,
    onNewUsername: (String) -> Unit,
    password: String,
    onNewPassword: (String) -> Unit,
    onLoginUser: () -> Unit,
    onLoginGuest: () -> Unit,
    onCreateUser: () -> Unit,
    loading: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .weight(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.login_username_field))
                },
                value = username,
                onValueChange = { onNewUsername(it) },
                singleLine = true,
            )
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.login_password_field))
                },
                value = password,
                onValueChange = { onNewPassword(it) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            GutterTalkButton(
                text = if (!loading) stringResource(R.string.login_login_button) else stringResource(
                    R.string.login_login_loading
                ),
                onClick = onLoginUser,
                enabled = !loading
            )
        }
        Row {
            Box(
                modifier = Modifier.weight(0.5f)
            ) {
                GutterTalkButton(
                    text = stringResource(R.string.login_guest_button),
                    onClick = onLoginGuest
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier.weight(0.5f)
            ) {
                GutterTalkButton(
                    text = stringResource(R.string.login_create_button),
                    onClick = onCreateUser
                )
            }
        }
    }
}

@Preview
@Composable
fun GutterTalkLoginScreenPreview() {
    GutterTalkLoginScreen(
        username = "",
        onNewUsername = {},
        password = "",
        onNewPassword = {},
        onLoginUser = {},
        onLoginGuest = {},
        onCreateUser = {},
        loading = false
    )
}

@Preview
@Composable
fun GutterTalkLoginScreenPreviewLoading() {
    GutterTalkLoginScreen(
        username = "",
        onNewUsername = {},
        password = "",
        onNewPassword = {},
        onLoginUser = {},
        onLoginGuest = {},
        onCreateUser = {},
        loading = true
    )
}
