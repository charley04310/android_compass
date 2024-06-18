package com.example.reply.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.reply.R

@Composable
fun CompassComponent(
    modifier: Modifier = Modifier,
    azimuth: Float
) {
    Image(
        modifier = modifier
            .size(500.dp)
            .clip(CircleShape)
            .rotate(azimuth),
        painter = painterResource(id = R.drawable.boussole),
        contentDescription = "Compass Image",
    )
}


