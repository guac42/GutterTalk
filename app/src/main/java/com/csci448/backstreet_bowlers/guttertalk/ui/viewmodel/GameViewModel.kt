package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.GameEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.GameIntent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state.GameState
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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

    private val physics = GamePhysicsEngine()
    private var physicsJob: Job? = null

    init {
        physics.init()
    }

    override fun handleIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.ThrowBall -> {
                Log.d(LOG_TAG, "Received throw intent: ${intent.swipeVelocityX} ${intent.swipeVelocityZ} ${intent.swipeSpin}")
                physics.throwBall(intent.swipeVelocityX, intent.swipeVelocityZ, intent.swipeSpin)
            }
            is GameIntent.BallSettled -> {
                viewModelScope.launch {
                    when (intent.pinsHit?.size) {
                        null -> _effectFlow.emit(GameEffect.Insult("Did you aim for the pins, or are you just testing the durability of the gutters?"))
                        0 -> _effectFlow.emit(GameEffect.Insult("Back to our regular scheduled program..."))
                        3 -> _effectFlow.emit(GameEffect.Insult("I guess you took the whole some is better than none to heart huh?"))
                        10 -> _effectFlow.emit(GameEffect.Insult("Wow, even a broken clock gets it right twice a day."))
                    }
                }
            }
            is GameIntent.ResetPins -> {}
        }
    }

    private fun startPhysicsLoop() {
        physicsJob = viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                val snap = physics.step(1f / 60f)
                _stateFlow.update { current ->
                    current.copy(
                        ballTransform = PhysicsTransform(
                            snap.ballPosX, snap.ballPosY, snap.ballPosZ,
                            snap.ballRotX, snap.ballRotY, snap.ballRotZ, snap.ballRotW
                        ),
                        pinTransforms = snap.pins.map { p ->
                            IndexedTransform(p.id, PhysicsTransform(
                                p.posX, p.posY, p.posZ,
                                p.rotX, p.rotY, p.rotZ, p.rotW
                            ))
                        },
                        phase = if (snap.allSettled &&
                            current.phase == GamePhase.ROLLING) GamePhase.SCORED
                        else current.phase
                    )
                }
                delay(16L)
            }
        }
    }
}