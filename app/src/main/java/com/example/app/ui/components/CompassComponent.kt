package com.example.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
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

    // change the image if no point is selected
    val image: Int;

    if (false) {
        image = R.drawable.boussole
    } else {
        image = R.drawable.point
    }
    Image(
        modifier = modifier
            .size(500.dp)
            .clip(CircleShape)
            .rotate(azimuth),
        painter = painterResource(id = image),
        contentDescription = "Compass Image",
    )
}


