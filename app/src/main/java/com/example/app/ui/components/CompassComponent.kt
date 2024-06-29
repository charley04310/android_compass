package com.example.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app.R

@Composable
fun CompassComponent(
    modifier: Modifier = Modifier,
    azimuth: Float
) {
    val compassImage = if (false) R.drawable.boussole else R.drawable.point
    val backgroundImage = if (true) R.drawable.arrow else null

    Box(
        modifier = modifier.size(500.dp),
        contentAlignment = Alignment.Center
    ) {
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
                .rotate(azimuth)
                .align(Alignment.Center)
        )
    }
}
