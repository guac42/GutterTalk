package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import com.csci448.backstreet_bowlers.guttertalk.R
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.SettingsEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.SettingsIntent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel
    internal constructor(
        savedStateHandle: SavedStateHandle,
        context: Context
    ) : ViewModel(), IViewModelContract<SettingsState, SettingsIntent, SettingsEffect>{
        companion object {
            private const val LOG_TAG = "448.SettingsViewModel"
        }
        private var _savedState: SettingsState by savedStateHandle.saved(
            key = "SAVED_STATE",
            init = {SettingsState() }
        )
    private val _stateFlow: MutableStateFlow<SettingsState> = MutableStateFlow(_savedState)
    override val stateFlow: StateFlow<SettingsState> = _stateFlow.asStateFlow()
    private val _effectFlow: MutableStateFlow<SettingsEffect?> = MutableStateFlow(null)
    override val effectFlow: SharedFlow<SettingsEffect?> = _effectFlow.asSharedFlow()
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.bowling_music).apply {
        isLooping = true
        setVolume(_savedState.musicVolume, _savedState.musicVolume)
        Log.d(LOG_TAG, "MediaPlayer created: $this")
    }


    override fun handleIntent(intent: SettingsIntent) {
        when(intent){
            is SettingsIntent.SetMusicEnabled -> {
                Log.d(LOG_TAG, "Settings music enabled: ${intent.enabled}")
                _stateFlow.update { state ->
                    _savedState = state.copy(isMusicOn = intent.enabled)
                    _savedState
                }
                if(intent.enabled){
                    mediaPlayer.start()
                } else {
                    mediaPlayer.pause()
                }
            }
            is SettingsIntent.SetMusicVolume -> {
                Log.d(LOG_TAG, "Setting music volume: ${intent.volume}")
                _stateFlow.update { state ->
                    _savedState = state.copy(musicVolume = intent.volume)
                    _savedState
                }
                mediaPlayer.setVolume(intent.volume, intent.volume)
            }
        }

    }

    override fun onCleared(){
        super.onCleared()
        mediaPlayer.release()
    }
}









