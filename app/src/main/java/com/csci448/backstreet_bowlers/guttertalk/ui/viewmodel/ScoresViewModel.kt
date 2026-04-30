package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.data.BowlingScoreRepository
import com.csci448.backstreet_bowlers.guttertalk.data.UserInformation
import com.csci448.backstreet_bowlers.guttertalk.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScoresViewModel(
    private val userRepository: UserRepository,
    private val scoreRepository: BowlingScoreRepository
) : ViewModel() {

    private val _userScores = MutableStateFlow<List<BowlingScore>>(emptyList())
    val userScores: StateFlow<List<BowlingScore>> = _userScores.asStateFlow()

    private val _topUsers = MutableStateFlow<List<UserInformation>>(emptyList())
    val topUsers: StateFlow<List<UserInformation>> = _topUsers.asStateFlow()

    fun loadUserScores(userId: String) {
        viewModelScope.launch {
            scoreRepository.getUserScores(userId).collect { scores ->
                _userScores.value = scores
            }
        }
    }

    fun loadGlobalTopScores(limit: Long = 10) {
        viewModelScope.launch {
            userRepository.getTopScores(limit).collect { users ->
                _topUsers.value = users
            }
        }
    }

    fun loadLocalTopScores(location: String, limit: Long = 10) {
        viewModelScope.launch {
            userRepository.getTopScores(limit, location).collect { users ->
                _topUsers.value = users
            }
        }
    }
}