package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras

class GutterTalkViewModelFactory : ViewModelProvider.Factory {
    companion object {
        private const val LOG_TAG = "448.GutterTalkViewModelFactory"
        private val CONTEXT_KEY = object : CreationExtras.Key<Context> {}
        fun creationExtras(defaultCreationExtras: CreationExtras, context: Context) =
            MutableCreationExtras(defaultCreationExtras).apply {
                set(CONTEXT_KEY, context)
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>, extras: CreationExtras
    ): T = with(modelClass) {
        when {
            isAssignableFrom(LoginViewModel::class.java) -> {
                Log.d(LOG_TAG, "creating LoginViewModel")
                val savedStateHandle = extras.createSavedStateHandle()
                LoginViewModel(savedStateHandle)
            }

            isAssignableFrom(GameViewModel::class.java) -> {
                Log.d(LOG_TAG, "creating GameViewModel")
                val savedStateHandle = extras.createSavedStateHandle()
                GameViewModel(savedStateHandle)
            }

            else -> {
                Log.e(LOG_TAG, "Unknown ViewModel: $modelClass")
                throw IllegalArgumentException("Unknown ViewModel: $modelClass")
            }
        }
    } as T
}