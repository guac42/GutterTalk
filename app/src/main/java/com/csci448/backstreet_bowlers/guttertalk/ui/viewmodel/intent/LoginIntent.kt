package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent

sealed class LoginIntent : GutterTalkIntent() {
    class NewUsernameString(val usernameString: String) : LoginIntent()
    class NewPasswordString(val passwordString: String) : LoginIntent()
    object Login : LoginIntent()
    object CreateUser: LoginIntent()
    object Clear : LoginIntent()

}