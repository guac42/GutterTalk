package com.csci448.backstreet_bowlers.guttertalk.util

import android.util.Log
import io.github.sceneview.math.localToWorldPosition
import kotlinx.coroutines.sync.Semaphore
import kotlinx.serialization.Serializable
import org.ode4j.math.*
import org.ode4j.ode.*


class GamePhysicsEngine {
    companion object {
        private const val LOG_TAG = "448.GamePhysicsEngine"
        const val LANE_LENGTH = 19.16
        const val LANE_WIDTH = 1.06
        const val LANE_THICKNESS = 0.1
        const val GUTTER_WIDTH = 0.23
        const val GUTTER_DEPTH = 0.047
        const val BALL_RADIUS = 0.108 // in meters
        const val PIN_HEIGHT = 0.38 // in meters
        const val PIN_RADIUS = 0.06 // in meters
    }

    private lateinit var world: DWorld
    private lateinit var space: DSpace
    private lateinit var contactGroup: DJointGroup
    private lateinit var ballBody: DBody
    private lateinit var ballGeom: DGeom
    private val pinBodies = mutableListOf<DBody>()
    private val pinGeoms = mutableListOf<DGeom>()

    private val ballMass = 6.8 // mass in kg
    private val ballRestitution = 0.6
    private val ballFriction = 0.15
    private val pinStartZ = 18.29 // distance to center of front pin
    private val pinMass = 1.53 // pin weight kg
    private val pinRestitution = 0.5

    private val semaphore = Semaphore(permits = 1)

    fun init() {
        OdeHelper.initODE2(0)

        world = OdeHelper.createWorld().apply {
            // erp           = 0.8     // error reduction (bounciness tuning)
            cfm           = 1e-5    // constraint force mixing (softness)
            quickStepNumIterations = 20
            setGravity(DVector3(0.0, -9.8, 0.0))
        }

        space = OdeHelper.createHashSpace()
        contactGroup = OdeHelper.createJointGroup()

        setupLane()
        setupGutters()
        setupBall()
        setupPins()
        resetPins()
    }

    suspend fun reset() {
        semaphore.acquire()

        ballBody.setPosition(0.0, BALL_RADIUS, -LANE_LENGTH/2)
        ballBody.linearVel = DVector3()
        ballBody.angularVel = DVector3()
        resetPins()

        semaphore.release()
    }

    private fun setupLane() {
        OdeHelper.createBox(space, DVector3(LANE_WIDTH, LANE_THICKNESS, LANE_LENGTH)).apply {
            setPosition(0.0, -LANE_THICKNESS/2, -LANE_LENGTH/2)
        }

        // Create a world boundary plane at y=-0.5
        OdeHelper.createPlane(space, 0.0, 1.0, 0.0, -0.5)
    }

    private fun setupGutters() {
        OdeHelper.createBox(space, DVector3(GUTTER_WIDTH, LANE_THICKNESS, LANE_LENGTH)).apply {
            setPosition(-LANE_WIDTH/2 - GUTTER_WIDTH/2, -LANE_THICKNESS/2-GUTTER_DEPTH, -LANE_LENGTH/2)
        }
        OdeHelper.createBox(space, DVector3(GUTTER_WIDTH, LANE_THICKNESS, LANE_LENGTH)).apply {
            setPosition(LANE_WIDTH/2 + GUTTER_WIDTH/2, -LANE_THICKNESS/2-GUTTER_DEPTH, -LANE_LENGTH/2)
        }
    }

    private fun setupBall() {
        ballBody = OdeHelper.createBody(world).apply {
            setPosition(0.0, BALL_RADIUS, -LANE_LENGTH/2)
            mass = OdeHelper.createMass().also { m ->
                m.setSphereTotal(ballMass, BALL_RADIUS)
                this.mass = m
            }
            angularDamping = 0.05
        }
        ballGeom = OdeHelper.createSphere(space, BALL_RADIUS).apply {
            body = ballBody
        }
    }

    private fun setupPins() {
        repeat(10) {
            val body = OdeHelper.createBody(world).apply {
                mass = OdeHelper.createMass().also { m ->
                    m.setCylinderTotal(pinMass, 2, PIN_RADIUS, PIN_HEIGHT)
                    this.mass = m
                }
                angularDamping = 0.1
            }
            // Capsule geom: good approximation for a pin shape
            val geom = OdeHelper.createCapsule(space, PIN_RADIUS, PIN_HEIGHT-2*PIN_RADIUS).apply {
                val rot = DMatrix3()
                OdeMath.dRFromAxisAndAngle(rot, 1.0, 0.0, 0.0, Math.PI/2)
                this.rotation = rot
                this.body = body
            }
            pinBodies.add(body)
            pinGeoms.add(geom)
        }
    }

    private fun resetPins() {
        val spacing = 0.305 // 12 inches between pin centers
        val pinFormation = listOf(
            // Row 1
            DVector3(0.0,         PIN_HEIGHT/2 + 0.2, -pinStartZ),
            // Row 2
            DVector3(-spacing/2,  PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 0.866),
            DVector3( spacing/2,  PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 0.866),
            // Row 3
            DVector3(-spacing,    PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 1.732),
            DVector3(0.0,         PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 1.732),
            DVector3( spacing,    PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 1.732),
            // Row 4
            DVector3(-spacing*1.5, PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 2.598),
            DVector3(-spacing/2,   PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 2.598),
            DVector3( spacing/2,   PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 2.598),
            DVector3( spacing*1.5, PIN_HEIGHT/2 + 0.2, -pinStartZ - spacing * 2.598)
        )

        pinFormation.forEachIndexed { index, pos ->
            pinBodies[index].setPosition(pos.get0(), pos.get1(), pos.get2())
            pinBodies[index].linearVel = DVector3()
            pinBodies[index].angularVel = DVector3()
        }
    }

    suspend fun throwBall(velocityX: Double, velocityZ: Double, spinY: Double) {
        semaphore.acquire()

        ballBody.setPosition(0.0, BALL_RADIUS, -LANE_LENGTH/2)
        ballBody.setLinearVel(velocityX, 0.0, -velocityZ)
        ballBody.setAngularVel(0.0, spinY, 0.0)

        semaphore.release()
    }

    suspend fun step(deltaTime: Double): PhysicsSnapshot3D {
        semaphore.acquire()

        space.collide(null, nearCallback)
        world.quickStep(deltaTime)

        semaphore.release()

        return buildSnapshot()
    }

    private val nearCallback = DGeom.DNearCallback { _, o1, o2 ->
        val b1 = o1.body
        val b2 = o2.body

        // Skip if both geoms are connected by a joint already
        if (b1 != null && b2 != null && OdeHelper.areConnected(b1, b2)) return@DNearCallback

        val MAX_CONTACTS = 10;
        val contacts = DContactBuffer(MAX_CONTACTS)

        val n = OdeHelper.collide(o1, o2, MAX_CONTACTS, contacts.geomBuffer)
        repeat(n) { i ->
            contacts[i].surface.apply {
                mode      = OdeConstants.dContactBounce or OdeConstants.dContactSoftCFM
                mu        = 0.6          // friction
                bounce    = 0.35         // restitution
                bounce_vel = 0.1         // minimum velocity for bounce
                soft_cfm = 0.001
            }
            OdeHelper.createContactJoint(world, contactGroup, contacts[i]).apply {
                attach(b1, b2)
            }
        }
    }

    private fun buildSnapshot(): PhysicsSnapshot3D {
        val ballPos = ballBody.position
        val ballRot = ballBody.quaternion

        val pins = pinBodies.mapIndexed { i, body ->
            val pos = body.position
            val rot = body.quaternion
            val vel = body.linearVel
            val ang = body.angularVel
            PinSnapshot3D(
                id     = i,
                posX   = pos.get0().toFloat(), posY = pos.get1().toFloat(), posZ = pos.get2().toFloat(),
                rotX   = rot.get1().toFloat(), rotY = rot.get2().toFloat(),
                rotZ   = rot.get3().toFloat(), rotW = rot.get0().toFloat(), // ode4j: w,x,y,z order
                isSettled = vel.length() < 0.05 && ang.length() < 0.05
            )
        }

        return PhysicsSnapshot3D(
            ballPosX = ballPos.get0().toFloat(),
            ballPosY = ballPos.get1().toFloat(),
            ballPosZ = ballPos.get2().toFloat(),
            ballRotX = ballRot.get1().toFloat(),
            ballRotY = ballRot.get2().toFloat(),
            ballRotZ = ballRot.get3().toFloat(),
            ballRotW = ballRot.get0().toFloat(),
            pins        = pins,
            allSettled  = pins.all { it.isSettled }
        )
    }
}

@Serializable
data class PhysicsSnapshot3D(
    val ballPosX: Float, val ballPosY: Float, val ballPosZ: Float,
    val ballRotX: Float, val ballRotY: Float, val ballRotZ: Float, val ballRotW: Float,
    val pins: List<PinSnapshot3D>,
    val allSettled: Boolean
)

@Serializable
data class PinSnapshot3D(
    val id: Int,
    val posX: Float, val posY: Float, val posZ: Float,
    val rotX: Float, val rotY: Float, val rotZ: Float, val rotW: Float,
    val isSettled: Boolean
)
