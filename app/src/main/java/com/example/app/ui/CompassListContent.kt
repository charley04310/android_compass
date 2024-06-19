package com.example.app.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.app.ui.components.CompassComponent

class CompassActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var rotationMatrix = FloatArray(9)
    private var orientationAngles = FloatArray(3)
    private var azimuth: Float by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also { rotationVector ->
            sensorManager.registerListener(
                this,
                rotationVector,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        setContent {
            CompassScreen(azimuth)
        }
    }

    @Composable
    fun CompassScreen(azimuth: Float) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CompassComponent(
                modifier = Modifier
                    .size(200.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .rotate(azimuth),
                azimuth = azimuth
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        println("HEREEEEE")
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            Log.d("CompassActivity", "Azimuth: $azimuth")
        } else {
            Log.d("CompassActivity", "Sensor event type is not TYPE_ROTATION_VECTOR")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
