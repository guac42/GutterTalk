package com.csci448.backstreet_bowlers.guttertalk.ui.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Size
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

@Composable
fun GutterTalkLaneScreen(
    modifier: Modifier = Modifier,
) {
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine)
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val cameraManipulator = rememberCameraManipulator()
    // val modelInstance = rememberModelInstance(modelLoader, "models/bowling_ball.glb")

    /*val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 20_000, easing = LinearEasing)),
        label = "rotationY"
    )*/

    val greenMaterial =
        remember(materialLoader) { materialLoader.createColorInstance(Color(0xFF4CAF50)) }
    val tanMaterial =
        remember(materialLoader) { materialLoader.createColorInstance(Color(0xFFD2B48C)) }

    Box(modifier = modifier.fillMaxSize()) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            environmentLoader = environmentLoader,
            cameraManipulator = cameraManipulator
        ) {
            SphereNode(
                materialInstance = greenMaterial,
                radius = 0.108f,
                position = Position(),
            )
            PlaneNode(
                materialInstance = tanMaterial,
                size = Size(1f, 0.1f, 1f),
                position = Position(y = -0.11f)
            )
            /*modelInstance?.let {
                ModelNode(
                    modelInstance = it,
                    scaleToUnits = 1.0f,
                    position = Position(x = 1.0f),
                    rotation = Rotation(y = rotationY)
                )
            }

            if (modelInstance == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }*/
        }
    }
}