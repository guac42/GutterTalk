package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect

sealed class LoginEffect : GutterTalkEffect() {
    object LoginSucceeded : LoginEffect()
    object LoginFailed : LoginEffect()
    object VerificationEmailSent: LoginEffect()
    object EmailNotVerified: LoginEffect()





}





