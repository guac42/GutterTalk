package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.GameEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.GameIntent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel
internal constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), IViewModelContract<GameState, GameIntent, GameEffect> {

    companion object {
        private const val LOG_TAG = "448.GameViewModel"
        private const val ALL_PINS_MASK = 0b1111111111  // 10 pins
    }

    private var _savedState: GameState by savedStateHandle.saved(
        key = "SAVED_GAME_STATE",
        init = { GameState() }
    )
    private val _stateFlow: MutableStateFlow<GameState> = MutableStateFlow(_savedState)
    override val stateFlow: StateFlow<GameState> = _stateFlow.asStateFlow()

    private val _effectFlow: MutableStateFlow<GameEffect?> = MutableStateFlow(null)
    override val effectFlow: SharedFlow<GameEffect?> = _effectFlow.asStateFlow()

    override fun handleIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.ThrowBall -> {}
            is GameIntent.BallSettled -> {
                if (intent.pinsHit == null) {
                    Log.d(LOG_TAG, "Received BallSettled intent for gutter ball!")
                } else {
                    Log.d(LOG_TAG, "Received BallSettled intent with ${intent.pinsHit.size} pins hit")
                }
            }
            is GameIntent.ResetPins -> {}
        }
    }
}