package com.epam.nyekilajos.coroutinespoc.position

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.AXIS_X
import android.hardware.SensorManager.AXIS_Z
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.zip
import kotlinx.coroutines.flow.asFlow
import kotlin.math.PI

@FlowPreview
@ExperimentalCoroutinesApi
class SensorPositionProvider {

    private val positionReadingsChannel = ConflatedBroadcastChannel<Position>()
    private val accelerometerReadings = Channel<FloatArray>(Channel.CONFLATED)
    private val magnetoMeterReadings = Channel<FloatArray>(Channel.CONFLATED)

    val positionReadings = positionReadingsChannel.asFlow()

    private val accelerometerListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // NOP
        }

        override fun onSensorChanged(event: SensorEvent?) {
            GlobalScope.launch(Dispatchers.Default) {
                event?.let { accelerometerReadings.send(it.values) }
            }
        }
    }

    private val magnetometerListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // NOP
        }

        override fun onSensorChanged(event: SensorEvent?) {
            GlobalScope.launch(Dispatchers.Default) {
                event?.let { magnetoMeterReadings.send(it.values) }
            }
        }
    }

    fun register(sensorManager: SensorManager) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            ?: throw IllegalStateException("No accelerometer found in the device")

        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            ?: throw IllegalStateException("No magnetometer found in the device")

        sensorManager.registerListener(
            accelerometerListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
        sensorManager.registerListener(
            magnetometerListener,
            magnetometer,
            SensorManager.SENSOR_DELAY_UI
        )

        val positions = accelerometerReadings
            .zip(magnetoMeterReadings) { acc, magnet ->
                convertReadingsToPosition(acc, magnet)
            }

        GlobalScope.launch(Dispatchers.Default) {
            positions.consumeEach {
                positionReadingsChannel.send(it)
            }
        }
    }

    fun unregister(sensorManager: SensorManager) {
        sensorManager.unregisterListener(accelerometerListener)
        sensorManager.unregisterListener(magnetometerListener)
    }

    private fun convertReadingsToPosition(acc: FloatArray, magnet: FloatArray): Position {
        // Rotation matrix based on current readings from accelerometer and magnetometer.
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(rotationMatrix, null, acc, magnet)

        val remappedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            AXIS_X,
            AXIS_Z,
            remappedRotationMatrix
        )

        // Express the updated rotation matrix as three orientation angles.
        val orientationAngles = FloatArray(3)
        SensorManager.getOrientation(remappedRotationMatrix, orientationAngles)

        return Position(orientationAngles)
    }

    companion object {
        val INSTANCE: SensorPositionProvider by lazy { SensorPositionProvider() }
    }
}

data class Position(val azimuth: Float, val pitch: Float, val roll: Float) {
    constructor(array: FloatArray) : this(array[0], array[1], array[2])
}

fun Float.toDegrees() = (this * 180 / PI).toInt()
