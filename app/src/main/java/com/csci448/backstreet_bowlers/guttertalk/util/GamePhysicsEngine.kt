package com.csci448.backstreet_bowlers.guttertalk.util

import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.collision.shapes.BoxCollisionShape
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.collision.shapes.PlaneCollisionShape
import com.jme3.bullet.collision.shapes.SphereCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Plane
import com.jme3.math.Vector3f

class GamePhysicsEngine {
    private lateinit var physicsSpace: PhysicsSpace
    private lateinit var ballBody: PhysicsRigidBody
    private val pinBodies = mutableListOf<PhysicsRigidBody>()

    private val laneLength = 19.16f
    private val laneWidth = 1.06f
    private val laneThickness = 0.1f
    private val gutterWidth = 0.23f
    private val gutterDepth = 0.047f
    private val ballRadius = 0.108f // radius in meters
    private val ballMass = 6.8f // mass in kg
    private val ballRestitution = 0.6f
    private val ballFriction = 0.15f
    private val pinStartZ = 18.29f // distance to center of front pin
    private val pinWeight = 1.53f // pin weight kg
    private val pinRestitution = 0.5f


    fun init() {
        physicsSpace = PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT)
        physicsSpace.setGravity(Vector3f(0f, -9.8f, 0f))

        setupLane()
        setupGutters()
        setupBall()
        setupPins()
    }

    private fun setupLane() {
        val floor = PhysicsRigidBody(BoxCollisionShape(Vector3f(laneWidth/2, laneThickness/2, laneLength/2)), 0f)
        floor.setPhysicsLocation(Vector3f(0f, -laneThickness/2, -laneLength/2))
        physicsSpace.addCollisionObject(floor)

        // Create a world boundary plane at y=-0.5
        val boundary = PhysicsRigidBody(PlaneCollisionShape(Plane(Vector3f(0f, 1f, 0f), -0.5f)), 0f)
        physicsSpace.addCollisionObject(boundary)
    }

    private fun setupGutters() {
        val gutterShape = BoxCollisionShape(Vector3f(gutterWidth/2, laneThickness/2, laneLength/2))
        listOf(-laneWidth/2 - gutterWidth/2, laneWidth/2 + gutterWidth/2).forEach { x ->
            val gutter = PhysicsRigidBody(gutterShape, 0f)
            gutter.setPhysicsLocation(Vector3f(x, -laneThickness/2-gutterDepth, 0f))
            physicsSpace.addCollisionObject(gutter)
        }
    }

    private fun setupBall() {
        val ballShape = SphereCollisionShape(ballRadius)
        ballBody = PhysicsRigidBody(ballShape, ballMass)
        ballBody.setPhysicsLocation(Vector3f(0f, ballRadius, -laneLength/2))
        ballBody.restitution = ballRestitution
        ballBody.friction = ballFriction
        ballBody.angularDamping = 0.5f
        physicsSpace.addCollisionObject(ballBody)
    }

    private fun setupPins() {
        pinBodies.clear()

        // radius 0.06m, height 0.38m
        val pinShape = CapsuleCollisionShape(0.06f, 0.26f)

        val spacing = 0.305f // 12 inches between pin centers
        val pinFormation = listOf(
            // Row 1
            Vector3f(0f,          0f, pinStartZ),
            // Row 2
            Vector3f(-spacing/2,  0f, pinStartZ + spacing * 0.866f),
            Vector3f( spacing/2,  0f, pinStartZ + spacing * 0.866f),
            // Row 3
            Vector3f(-spacing,    0f, pinStartZ + spacing * 1.732f),
            Vector3f(0f,          0f, pinStartZ + spacing * 1.732f),
            Vector3f( spacing,    0f, pinStartZ + spacing * 1.732f),
            // Row 4
            Vector3f(-spacing*1.5f, 0f, pinStartZ + spacing * 2.598f),
            Vector3f(-spacing/2,    0f, pinStartZ + spacing * 2.598f),
            Vector3f( spacing/2,    0f, pinStartZ + spacing * 2.598f),
            Vector3f( spacing*1.5f, 0f, pinStartZ + spacing * 2.598f)
        )

        pinFormation.forEachIndexed { index, pos ->
            val pin = PhysicsRigidBody(pinShape, pinWeight)
            pin.setPhysicsLocation(Vector3f(pos.x, 0.19f, pos.z))
            pin.restitution = pinRestitution
            pin.friction = 0.4f
            physicsSpace.addCollisionObject(pin)
            pinBodies.add(pin)
        }
    }

    fun throwBall(velocityX: Float, velocityZ: Float, spinY: Float) {
        ballBody.setPhysicsLocation(Vector3f(0f, ballRadius, -laneLength/2))
        ballBody.setLinearFactor(Vector3f(velocityX, 0f, velocityZ))
        ballBody.setAngularVelocity(Vector3f(0f, spinY, 0f))
    }

    fun step(deltaTime: Float): PhysicsSnapshot3D {
        physicsSpace.update(deltaTime)

        val ballPos = ballBody.getPhysicsLocation(null)
        val ballRot = ballBody.getPhysicsRotation(null)

        val pins = pinBodies.mapIndexed { i, body ->
            val pos = body.getPhysicsLocation(null)
            val rot = body.getPhysicsRotation(null)
            PinSnapshot3D(
                id = i,
                posX = pos.x, posY = pos.y, posZ = pos.z,
                rotX = rot.x, rotY = rot.y, rotZ = rot.z, rotW = rot.w,
                isSettled = body.getLinearVelocity(null).length() < 0.05f &&
                        body.getLinearVelocity(null).length() < 0.05f
            )
        }

        PhysicsSnapshot3D(
            ballPosX = ballPos.x, ballPosY = ballPos.y, ballPosZ = ballPos.z,
            ballRotX = ballRot.x, ballRotY = ballRot.y,
            ballRotZ = ballRot.z, ballRotW = ballRot.w,
            pins = pins,
            allSettled = pins.all { it.isSettled }
        )
    }

    fun reset() {
        pinBodies.forEach { physicsSpace.removeCollisionObject(it) }
        physicsSpace.removeCollisionObject(ballBody)
        pinBodies.clear()
        setupBall()
        setupPins()
    }
}

data class PhysicsSnapshot3D(
    val ballPosX: Float, val ballPosY: Float, val ballPosZ: Float,
    val ballRotX: Float, val ballRotY: Float, val ballRotZ: Float, val ballRotW: Float,
    val pins: List<PinSnapshot3D>,
    val allSettled: Boolean
)

data class PinSnapshot3D(
    val id: Int,
    val posX: Float, val posY: Float, val posZ: Float,
    val rotX: Float, val rotY: Float, val rotZ: Float, val rotW: Float,
    val isSettled: Boolean
)
