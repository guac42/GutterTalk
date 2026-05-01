package com.csci448.backstreet_bowlers.guttertalk.ui.game

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Size
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import androidx.compose.ui.platform.LocalContext

@Composable
fun GutterTalkLaneScreen(
    modifier: Modifier = Modifier,
    onBallSettled: (Set<Int>?) -> Unit,
    isInsultsOn: Boolean
) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine)
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val cameraManipulator = rememberCameraManipulator()

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
        }

        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Controls", style = MaterialTheme.typography.titleMedium)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            if (isInsultsOn) {
                                Toast.makeText(context, "Did you aim for the pins, or are you just testing the durability of the gutters?",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Gutter")
                        }
                        Button(onClick = {
                            if (isInsultsOn) {
                                Toast.makeText(context, "Back to our regular scheduled program...",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Miss")
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            if (isInsultsOn) {
                                Toast.makeText(context, "I guess you took the whole some is better than none to heart huh?",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("3 pins")
                        }
                        Button(onClick = {
                            if (isInsultsOn) {
                                Toast.makeText(context, "Wow, even a broken clock gets it right twice a day.",
                                    Toast.LENGTH_LONG).show()
                            }
                        }) {
                            Text("Strike")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GutterTalkLaneControlsPreview() {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text("Controls", style = MaterialTheme.typography.titleMedium)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { }) {
                        Text("Gutter")
                    }
                    Button(onClick = { }) {
                        Text("Miss")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { }) {
                        Text("3 pins")
                    }
                    Button(onClick = { }) {
                        Text("Strike")
                    }
                }
            }
        }
    }
}