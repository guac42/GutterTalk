package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScoreRepository
import com.csci448.backstreet_bowlers.guttertalk.data.UserInformation
import com.csci448.backstreet_bowlers.guttertalk.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class ScoresViewModel(
    private val userRepository: UserRepository,
    private val scoreRepository: BowlingScoreRepository
) : ViewModel() {
    private val LOG_TAG = "ScoresViewModel"

    private val _userScores = MutableStateFlow<List<BowlingScore>>(emptyList())
    val userScores: StateFlow<List<BowlingScore>> = _userScores.asStateFlow()

    private val _topUsers = MutableStateFlow<List<UserInformation>?>(emptyList())
    val topUsers: StateFlow<List<UserInformation>?> = _topUsers.asStateFlow()

    private val _userStats = MutableStateFlow<UserInformation?>(null)
    val userStats: StateFlow<UserInformation?> = _userStats.asStateFlow()

    fun loadUserScores(userId: String) {
        viewModelScope.launch {
            scoreRepository.getUserScores(userId).collect { scores ->
                _userScores.value = scores
            }
        }
    }

    fun loadUserInformation(userId: String){
        viewModelScope.launch {
            userRepository.getUser(userId)
            try{
                val info = userRepository.getUser(userId)
                _userStats.value = info
            } catch (e: Exception){
                Log.e(LOG_TAG, "Error loading user states", e)
                _userStats.value = null
            }
        }
    }


    fun loadLeaderboard(limit: Long = 10, location: String? = null) {
        viewModelScope.launch {
            _topUsers.value = null
            userRepository.getLeaderboard(limit,location)
                .catch{ e ->
                    Log.e(LOG_TAG, "Error getting leaderboard",e)
                    _topUsers.value = emptyList()
                }
                .collect { users ->
                _topUsers.value = users
            }
        }
    }
}