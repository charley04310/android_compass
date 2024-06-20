package com.example.app.ui.components

import android.R
import android.telecom.Call
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.observable.model.Response
import com.mapbox.search.ui.view.SearchResultsView

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val mapViewportState = remember { MapViewportState().apply {
        setCameraOptions {
            zoom(2.0)
            center(Point.fromLngLat(-98.0, 39.5))
            pitch(0.0)
            bearing(0.0)
        }
    }}


    Column(modifier = modifier.fillMaxSize()) {

        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState
        )
    }
}
