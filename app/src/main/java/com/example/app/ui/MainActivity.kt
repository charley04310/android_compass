package com.example.app.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.app.AzimuthStore
import com.example.app.ui.theme.ContrastAwareAppTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity(), SensorEventListener {

    private val viewModel: AppHomeViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private var rotationMatrix = FloatArray(9)
    private var orientationAngles = FloatArray(3)
    private var azimuth by mutableStateOf(0f)
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Permission granted")
        } else {
            Log.d("MainActivity", "Permission denied")
            // Handle permission denied case
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Log.d("MainActivity", "onCreate")


        //Last position
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                }
            }

        Log.d("MainActivity", "Latitude: $userLatitude")
        Log.d("MainActivity", "Longitude: $userLongitude")

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available
            Log.d("MainActivity", "Permission already granted")
        } else {
            // Permission is not granted, request it
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

                App(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    azimuth = azimuth,
                    userLatitude = userLatitude,
                    userLongitude = userLongitude
                )
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val rawAzimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat() + AzimuthStore.getAzimuth()
            azimuth = if (rawAzimuth < 0) rawAzimuth + 360 else rawAzimuth
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // do nothing
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
