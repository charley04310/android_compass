package com.example.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.AzimuthStore
import com.example.app.R

@Composable
fun CompassComponent(
    modifier: Modifier = Modifier,
    azimuth: Float
) {
    // Fetch the azimuth value from AzimuthStore

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(500.dp),
            contentAlignment = Alignment.Center
        ) {
            val compassImage = if (AzimuthStore.getAzimuth() == 0f) R.drawable.boussole else R.drawable.point
            val backgroundImage = if (AzimuthStore.getAzimuth() != 0f) R.drawable.arrow else null

            backgroundImage?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .rotate(-90.0F)
                        .align(Alignment.Center)
                )
            }

            Image(
                painter = painterResource(id = compassImage),
                contentDescription = "Compass Image",
                modifier = Modifier
                    .size(500.dp)
                    .clip(CircleShape)
                    .rotate(azimuth ?: 0f)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Align the red point with the arrow by moving the phone.",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to reset azimuth to 0f
        Button(
            onClick = { AzimuthStore.setAzimuth(0f) },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Reset Selected Place")
        }
    }
}
