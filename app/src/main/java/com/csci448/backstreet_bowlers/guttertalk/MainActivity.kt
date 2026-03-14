package com.csci448.backstreet_bowlers.guttertalk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.csci448.backstreet_bowlers.guttertalk.ui.navigation.GutterTalkNavHost
import com.csci448.backstreet_bowlers.guttertalk.ui.navigation.GutterTalkTopBar
import com.csci448.backstreet_bowlers.guttertalk.ui.theme.GutterTalkTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val LOG_TAG = "448.MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityContent()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.d(LOG_TAG, "onPostResume() called")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(LOG_TAG, "onAttachedToWindow() called")
    }

    override fun onContentChanged() {
        super.onContentChanged()
        Log.d(LOG_TAG, "onContentChanged() called")
    }

    override fun onDetachedFromWindow() {
        Log.d(LOG_TAG, "onDetachedFromWindow() called")
        super.onDetachedFromWindow()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        Log.d(LOG_TAG, "onEnterAnimationComplete() called")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.d(LOG_TAG, "onPostCreate() called")
    }

    override fun isFinishing(): Boolean {
        val areWeFinishing = super.isFinishing()
        Log.d(LOG_TAG, "isFinishing() called = $areWeFinishing")
        return areWeFinishing
    }

    override fun finish() {
        Log.d(LOG_TAG, "finish() called")
        super.finish()
    }
}

@Composable
private fun MainActivityContent() {
    val navController = rememberNavController()

    GutterTalkTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { GutterTalkTopBar(navController) }) { innerPadding ->
            GutterTalkNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewMainActivityContent() {
    MainActivityContent()
}
