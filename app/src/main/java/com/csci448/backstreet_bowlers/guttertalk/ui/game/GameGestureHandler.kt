package com.csci448.backstreet_bowlers.guttertalk.ui.game

import android.content.Context
import android.view.MotionEvent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.GameIntent
import dev.romainguy.kotlin.math.Float2
import io.github.sceneview.gesture.GestureDetector
import io.github.sceneview.node.Node

class GameGestureHandler(
    private val onThrow: (GameIntent.ThrowBall) -> Unit
) : GestureDetector.SimpleOnGestureListener() {
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, node: Node?, velocity: Float2) {
        // TODO: This could be the source of some input bugs
        if (e1 == null) return;

        // Only throw on upward swipe
        if (velocity.y >= 0) return

        val swipeCurve = (e2.x - e1.x)      // positive = curved right
        val forwardSpeed = (-velocity.y / 200f)       // tune divisor for feel
            .coerceIn(5f, 20f)                       // clamp to realistic m/s
        val aimX = (velocity.x / 1000f).coerceIn(-3f, 3f)
        val spin  = swipeCurve / 300f                // hook effect

        onThrow(GameIntent.ThrowBall(aimX, forwardSpeed, spin))
    }
}