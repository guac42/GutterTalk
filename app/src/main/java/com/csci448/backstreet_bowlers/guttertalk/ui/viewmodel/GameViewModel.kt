package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.data.database.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.data.database.BowlingScoreRepository
import com.csci448.backstreet_bowlers.guttertalk.data.database.UserRepository
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.GameEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.GameIntent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state.GameState
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max

class GameViewModel
internal constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val bowlingRepository: BowlingScoreRepository
) : ViewModel(), IViewModelContract<GameState, GameIntent, GameEffect> {

    companion object {
        private const val LOG_TAG = "448.GameViewModel"
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

    init {
        physics.init()

        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                val updateTime = System.currentTimeMillis()
                val snap = physics.step(1.0 / 60.0)
                _stateFlow.update { current ->
                    current.copy(
                        physicsSnapshot = snap,
                    )
                }
                if (_stateFlow.value.ballInMotion && physics.settled()) {
                    _stateFlow.update { current ->
                        val scores = current.rolls.toMutableList()
                        when (current.throwInFrame) {
                            0 -> {
                                val knocked = physics.knocked()
                                scores.add(knocked)
                                handlePinsHit(knocked)
                                physics.safeResetBall()
                                if (knocked == 10) {
                                    physics.safeResetPins()
                                }
                                current.copy(
                                    ballInMotion = false,
                                    knockedPins = knocked,
                                    rolls = scores,
                                    throwInFrame = 1
                                )
                            }
                            1 -> {
                                // TODO: This is wrong because if we hit two strikes the second will not count
                                val knocked = max(0, physics.knocked() - current.knockedPins)
                                scores.add(knocked)
                                handlePinsHit(knocked)
                                if (current.currentFrame < 10 || physics.knocked() == 10) {
                                    physics.safeReset()
                                } else {
                                    physics.safeResetBall()
                                }
                                current.copy(
                                    ballInMotion = false,
                                    throwInFrame = if (current.currentFrame < 10) 0 else 2,
                                    knockedPins = if (current.currentFrame < 10) 0 else physics.knocked(),
                                    rolls = scores,
                                    currentFrame = if (current.currentFrame < 10) current.currentFrame+1 else current.currentFrame
                                )
                            }
                            2 -> {
                                val knocked = max(0, physics.knocked() - current.knockedPins)
                                scores.add(knocked)
                                physics.safeReset()
                                current.copy(
                                    ballInMotion = false,
                                    throwInFrame = 0,
                                    knockedPins = 0,
                                    rolls = scores,
                                    currentFrame = current.currentFrame+1
                                )
                            }
                            else -> throw RuntimeException("Too many throws in frame")
                        }
                    }
                }
                val nextUpdate = updateTime + 1000.0 / 60
                delay((nextUpdate - System.currentTimeMillis()).toLong())
            }
        }
    }

    override fun handleIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.ThrowBall -> {
                Log.d(LOG_TAG, "Received throw intent: ${intent.swipeVelocityX} ${intent.swipeVelocityZ} ${intent.swipeSpin}")
                viewModelScope.launch {
                    _effectFlow.emit(null)
                    _stateFlow.update { current ->
                        current.copy(
                            ballInMotion = true
                        )
                    }
                    physics.throwBall(
                        -intent.swipeVelocityX,
                        intent.swipeVelocityZ,
                        intent.swipeSpin
                    )
                }
            }
            is GameIntent.BallSettled -> {
            }
            is GameIntent.UpdateScore -> {
                viewModelScope.launch {
                    Log.d(LOG_TAG, "Triggering update score")
                    bowlingRepository.addScore(
                        intent.gameState
                    )
                    userRepository.updateUserStats(
                        userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid!!,
                        score = intent.gameState.score!!,
                        strikes = calcStrikes(intent.gameState.rolls),
                        spares = calcSpares(intent.gameState.rolls)

                    )
                }
            }
            is GameIntent.NewGame -> {
                viewModelScope.launch {
                    physics.safeReset()
                    _stateFlow.update { current ->
                        current.copy(
                            ballInMotion = false,
                            knockedPins = 0,
                            rolls = emptyList(),
                            currentFrame = 1,
                            throwInFrame = 0
                        )
                    }
                }
            }
        }
    }

    private fun handlePinsHit(count: Int?) {
        viewModelScope.launch {
            when (count) {
                null -> _effectFlow.emit(GameEffect.Insult("Did you aim for the pins, or are you just testing the durability of the gutters?"))
                in 0..2 -> _effectFlow.emit(GameEffect.Insult("Back to our regular scheduled program..."))
                in 3..9 -> _effectFlow.emit(GameEffect.Insult("I guess you took the whole some is better than none to heart huh?"))
                10 -> _effectFlow.emit(GameEffect.Insult("Wow, even a broken clock gets it right twice a day."))
            }
        }
    }

    private fun calcStrikes(rolls: List<Int?>): Int{
        var strikes = 0
        var rollIndex = 0

        // Standard bowling game has 10 frames
        for (frame in 1..10) {
            if (rollIndex >= rolls.size) break

            val firstRoll = rolls[rollIndex] ?: 0
            if (firstRoll == 10) {
                strikes++
                rollIndex += 1 // Strike moves to next frame
            } else {
                rollIndex += 2 // Non-strike consumes two rolls in a frame
            }
        }
        return strikes
    }

    private fun calcSpares(rolls: List<Int?>): Int {
        var spares = 0
        var rollIndex = 0

        for (frame in 1..10) {
            if (rollIndex + 1 >= rolls.size) break

            val firstRoll = rolls[rollIndex] ?: 0
            if (firstRoll == 10) {
                rollIndex += 1
            } else {
                val secondRoll = rolls[rollIndex + 1] ?: 0
                if (firstRoll + secondRoll == 10) {
                    spares++
                }
                rollIndex += 2
            }
        }
        return spares
    }
}