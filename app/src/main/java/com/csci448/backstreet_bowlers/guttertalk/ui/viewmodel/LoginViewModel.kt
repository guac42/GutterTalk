package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.LoginEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.LoginIntent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel
internal constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), IViewModelContract<LoginState, LoginIntent, LoginEffect> {
    companion object {
        private const val LOG_TAG = "448.LoginViewModel"
    }

    private var _savedState: LoginState by savedStateHandle.saved(
        key = "SAVED_LOGIN_STATE",
        init = { LoginState() }
    )
    private val _stateFlow: MutableStateFlow<LoginState> = MutableStateFlow(_savedState)
    override val stateFlow: StateFlow<LoginState> = _stateFlow.asStateFlow()

    private val _effectFlow: MutableStateFlow<LoginEffect?> = MutableStateFlow(null)
    override val effectFlow: SharedFlow<LoginEffect?> = _effectFlow.asSharedFlow()

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.NewUsernameString -> {
                Log.d(LOG_TAG, "Handling update username string")
                _stateFlow.update { state ->
                    _savedState = state.copy(
                        username = intent.usernameString
                    )
                    _savedState
                }
            }

            is LoginIntent.NewPasswordString -> {
                Log.d(LOG_TAG, "Handling update password string")
                _stateFlow.update { state ->
                    _savedState = state.copy(
                        password = intent.passwordString
                    )
                    _savedState
                }
            }

            is LoginIntent.Login -> {
                Log.d(LOG_TAG, "Handling login intent")
                _stateFlow.update { state ->
                    _savedState = state.copy(loading = true)
                    _savedState
                }
                viewModelScope.launch {
                    com.google.firebase.auth.FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(
                            _stateFlow.value.username,
                            _stateFlow.value.password
                        )
                        .addOnSuccessListener {
                            Log.d(LOG_TAG, "Firebase login succeeded")
                            _stateFlow.update { state ->
                                _savedState = state.copy(
                                    loading = false,
                                    username = "",
                                    password = ""
                                )
                                _savedState
                            }
                            _effectFlow.update { LoginEffect.LoginSucceeded }
                        }
                        .addOnFailureListener {
                            Log.d(LOG_TAG, "Firebase login failed: ${it.message}")
                            _stateFlow.update { state ->
                                _savedState = state.copy(loading = false)
                                _savedState
                            }
                            _effectFlow.update { LoginEffect.LoginFailed }
                        }


                }
            }

            is LoginIntent.CreateUser -> {
                Log.d(LOG_TAG, "Handling create user intent")
                _stateFlow.update { state ->
                    _savedState = state.copy(loading = true)
                    _savedState
                }
                viewModelScope.launch {
                    com.google.firebase.auth.FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(
                            _stateFlow.value.username,
                            _stateFlow.value.password
                        )
                        .addOnSuccessListener {
                            Log.d(LOG_TAG, "Firebase create user succeeded")
                            _stateFlow.update { state ->
                                _savedState = state.copy(
                                    loading = false,
                                    username = "",
                                    password = ""
                                )
                                _savedState
                            }
                            _effectFlow.update { LoginEffect.LoginSucceeded }
                        }
                        .addOnFailureListener {
                            Log.d(LOG_TAG, "Firebase create user failed: ${it.message}")
                            _stateFlow.update { state ->
                                _savedState = state.copy(loading = false)
                                _savedState
                            }
                            _effectFlow.update { LoginEffect.LoginFailed }
                        }
                }
            }
            is LoginIntent.Clear -> {
                _effectFlow.update { null }
            }
        }


    }
}


