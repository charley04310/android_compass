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
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app.ui.theme.ContrastAwareAppTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures


class MainActivity : ComponentActivity(), SensorEventListener {
    private val viewModel: AppHomeViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var rotationMatrix = FloatArray(9)
    private var orientationAngles = FloatArray(3)
    private var azimuth by mutableStateOf(0f)


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CompassActivity", "EXECUTED ")


        enableEdgeToEdge()
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
            ContrastAwareAppTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                App(

                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    azimuth = azimuth,

                )

            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val rawAzimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            azimuth = if (rawAzimuth < 0) rawAzimuth + 360 else rawAzimuth
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // do nothing
    }


}
