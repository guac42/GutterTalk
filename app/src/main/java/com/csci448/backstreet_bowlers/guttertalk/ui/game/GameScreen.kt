package com.csci448.backstreet_bowlers.guttertalk.ui.game

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.data.database.BowlingScore
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.GutterTalkScoreboard
import com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores.ScoreCalculator
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.GameIntent
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.BALL_RADIUS
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.GUTTER_DEPTH
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.GUTTER_WIDTH
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.LANE_LENGTH
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.LANE_THICKNESS
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.LANE_WIDTH
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.PIN_HEIGHT
import com.csci448.backstreet_bowlers.guttertalk.util.GamePhysicsEngine.Companion.PIN_RADIUS
import com.csci448.backstreet_bowlers.guttertalk.util.PhysicsSnapshot3D
import com.csci448.backstreet_bowlers.guttertalk.util.PinSnapshot3D
import dev.romainguy.kotlin.math.Quaternion
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.math.toRotation
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView

private const val LOG_TAG = "448.LaneScreenSpec"

@Composable
fun GutterTalkLaneScreen(
    modifier: Modifier = Modifier,
    onThrow: (GameIntent.ThrowBall) -> Unit,
    physicsSnapshot: PhysicsSnapshot3D?,
    rolls: List<Int>,
    frame: Int
) {
    val engine = rememberEngine()
    val engineView = rememberView(engine)
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val collisionSystem = rememberCollisionSystem(engineView)
    val gestureHandler = remember { GameGestureHandler(onThrow) }

    // Material instances
    val gutterMaterial =
        remember(materialLoader) { materialLoader.createColorInstance(Color.DarkGray) }
    val laneMaterial =
        remember(materialLoader) { materialLoader.createColorInstance(Color(0xFF805736)) }
    val pinMaterial = remember(materialLoader) { materialLoader.createColorInstance(Color.White) }
    val ballMaterial =
        remember(materialLoader) { materialLoader.createColorInstance(Color(0xFF4CAF50)) }

    val cameraNode = rememberCameraNode(engine) {
        position = Position(0f, 4f, 4f)
        lookAt(Position(0f, 0f, 16f))
    }

    Box(modifier = modifier.fillMaxSize()) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            collisionSystem = collisionSystem,
            cameraNode = cameraNode,
            cameraManipulator = null,
            onGestureListener = gestureHandler,
        ) {
            CubeNode(
                materialInstance = laneMaterial,
                size = Scale(LANE_WIDTH.toFloat(), LANE_THICKNESS.toFloat(), LANE_LENGTH.toFloat()),
                position = Position(0f, -LANE_THICKNESS.toFloat() / 2, LANE_LENGTH.toFloat() / 2)
            )

            // Lane gutters
            CubeNode(
                materialInstance = gutterMaterial,
                size = Scale(
                    GUTTER_WIDTH.toFloat(),
                    LANE_THICKNESS.toFloat(),
                    LANE_LENGTH.toFloat()
                ),
                position = Position(
                    (-LANE_WIDTH / 2 - GUTTER_WIDTH / 2).toFloat(),
                    (-LANE_THICKNESS.toFloat() / 2 - GUTTER_DEPTH).toFloat(),
                    LANE_LENGTH.toFloat() / 2
                )
            )
            CubeNode(
                materialInstance = gutterMaterial,
                size = Scale(
                    GUTTER_WIDTH.toFloat(),
                    LANE_THICKNESS.toFloat(),
                    LANE_LENGTH.toFloat()
                ),
                position = Position(
                    (LANE_WIDTH / 2 + GUTTER_WIDTH / 2).toFloat(),
                    (-LANE_THICKNESS.toFloat() / 2 - GUTTER_DEPTH).toFloat(),
                    LANE_LENGTH.toFloat() / 2
                )
            )

            if (physicsSnapshot != null) {
                physicsSnapshot.pins.forEach { pin ->
                    CylinderNode(
                        materialInstance = pinMaterial,
                        radius = PIN_RADIUS.toFloat(),
                        height = PIN_HEIGHT.toFloat(),
                        position = pin.toPosition(),
                        rotation = pin.toRotation()
                    )
                }
                SphereNode(
                    materialInstance = ballMaterial,
                    radius = BALL_RADIUS.toFloat(),
                    position = physicsSnapshot.toPosition(),
                    rotation = physicsSnapshot.toQuaternion().toRotation()
                )
            }
        }
    }

    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        val rollsFilled = rolls.toMutableList<Int?>().apply {
            repeat(21-rolls.size) {
                add(null)
            }
        }
        val scoreCard = BowlingScore(
            rolls = rollsFilled
        )
        val scores: MutableList<Int?> = mutableListOf()
        repeat(10) { i ->
            scores += (ScoreCalculator(scoreCard, i+1))
        }

        GutterTalkScoreboard(
            modifier = Modifier.fillMaxWidth(),
            scoreCard = BowlingScore(
                rolls = rollsFilled,
                scores = scores
            )
        )
        /*Column(modifier = Modifier.padding(16.dp)) {
            Text("Controls ${physicsSnapshot?.pins?.count { it.isSettled }}", style = MaterialTheme.typography.titleMedium)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        onBallSettled(null)
                    }) {
                        Text("Gutter")
                    }
                    Button(onClick = {
                        onBallSettled(emptySet())
                    }) {
                        Text("Miss")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        onBallSettled(setOf(1, 2, 3))
                    }) {
                        Text("3 pins")
                    }
                    Button(onClick = {
                        onBallSettled(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
                    }) {
                        Text("Strike")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        physicsSnapshot?.pins?.forEach {
                            Log.d(LOG_TAG, it.posY.toString())
                        }
                    }) {
                        Text("Test")
                    }
                    Button(onClick = onReset) {
                        Text("Reset")
                    }
                }
            }
        }*/
    }
}

fun PhysicsSnapshot3D.toPosition() = Position(
    x = ballPosX,
    y = ballPosY,
    z = ballPosZ
)

fun PhysicsSnapshot3D.toQuaternion() = Quaternion(
    x = ballRotX,
    y = ballRotY,
    z = ballRotZ,
    w = ballRotW
)

fun PinSnapshot3D.toPosition() = Position(
    x = posX,
    y = posY,
    z = posZ
)

fun PinSnapshot3D.toRotation() = Quaternion(
    x = rotX,
    y = rotY,
    z = rotZ,
    w = rotW
).toRotation()

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

/**
 * Physics body with position, velocity, and collision properties.
 */
data class PhysicsBody(
    var x: Float,
    var y: Float,
    var z: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var vz: Float = 0f,
    val mass: Float = 1f,
    val radius: Float = 0.1f,
    val shape: String = "sphere",
    val isStatic: Boolean = false,
)

/**
 * Simple Euler integration physics step with ground collision and sphere-sphere collision.
 */
/*private fun updatePhysics(
    bodies: MutableList<PhysicsBody>,
    gravity: Float,
    bounciness: Float,
    dt: Float,
) {
    for (body in bodies) {
        if (body.isStatic) continue

        // Apply gravity
        body.vy += gravity * dt

        // Update position
        body.x += body.vx * dt
        body.y += body.vy * dt
        body.z += body.vz * dt

        // Ground collision (y = 0 plane)
        if (body.y - body.radius < 0f) {
            body.y = body.radius
            body.vy = -body.vy * bounciness
            // Friction
            body.vx *= 0.98f
            body.vz *= 0.98f
        }

        // Wall bounds (-5 to 5)
        if (body.x < -5f + body.radius || body.x > 5f - body.radius) {
            body.vx = -body.vx * bounciness
            body.x = body.x.coerceIn(-5f + body.radius, 5f - body.radius)
        }
        if (body.z < -5f + body.radius || body.z > 5f - body.radius) {
            body.vz = -body.vz * bounciness
            body.z = body.z.coerceIn(-5f + body.radius, 5f - body.radius)
        }
    }

    // Sphere-sphere collision detection
    for (i in bodies.indices) {
        for (j in i + 1 until bodies.size) {
            val a = bodies[i]
            val b = bodies[j]
            if (a.isStatic && b.isStatic) continue

            val dx = b.x - a.x
            val dy = b.y - a.y
            val dz = b.z - a.z
            val dist = sqrt(dx * dx + dy * dy + dz * dz)
            val minDist = a.radius + b.radius

            if (dist < minDist && dist > 0.001f) {
                // Elastic collision response
                val nx = dx / dist
                val ny = dy / dist
                val nz = dz / dist
                val relVx = a.vx - b.vx
                val relVy = a.vy - b.vy
                val relVz = a.vz - b.vz
                val velAlongNormal = relVx * nx + relVy * ny + relVz * nz

                if (velAlongNormal > 0) {
                    val impulse = velAlongNormal * bounciness
                    if (!a.isStatic) {
                        a.vx -= impulse * nx
                        a.vy -= impulse * ny
                        a.vz -= impulse * nz
                    }
                    if (!b.isStatic) {
                        b.vx += impulse * nx
                        b.vy += impulse * ny
                        b.vz += impulse * nz
                    }
                }

                // Separate overlapping bodies
                val overlap = minDist - dist
                if (!a.isStatic) {
                    a.x -= nx * overlap * 0.5f
                    a.y -= ny * overlap * 0.5f
                    a.z -= nz * overlap * 0.5f
                }
                if (!b.isStatic) {
                    b.x += nx * overlap * 0.5f
                    b.y += ny * overlap * 0.5f
                    b.z += nz * overlap * 0.5f
                }
            }
        }
    }
}

private fun createBowlingBodies(): List<PhysicsBody> {
    val bodies = mutableListOf<PhysicsBody>()
    // Bowling ball
    bodies.add(PhysicsBody(x = 0f, y = 0.15f, z = 4f, vz = -3f, radius = 0.15f, mass = 5f, shape = "sphere"))
    // 10 pins in triangle formation
    val pinPositions = listOf(
        0f to -3f, -0.15f to -3.3f, 0.15f to -3.3f,
        -0.3f to -3.6f, 0f to -3.6f, 0.3f to -3.6f,
        -0.45f to -3.9f, -0.15f to -3.9f, 0.15f to -3.9f, 0.45f to -3.9f,
    )
    for ((px, pz) in pinPositions) {
        bodies.add(PhysicsBody(x = px, y = 0.15f, z = pz, radius = 0.05f, mass = 0.5f, shape = "cylinder"))
    }
    return bodies
}*/
