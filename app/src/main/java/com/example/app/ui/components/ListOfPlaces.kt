package com.example.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.AzimuthStore

data class Place(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

@Composable
fun FamousPlacesScreen(lat: Double, lon: Double) {
    val places = listOf(
        Place("Eiffel Tower", "An iconic symbol of Paris and France.", 48.8584, 2.2945),
        Place("Great Wall of China", "An ancient series of walls and fortifications.", 40.4319, 116.5704),
        Place("Pyramids of Giza", "Ancient pyramids located in Egypt.", 29.9792, 31.1342),
        Place("Statue of Liberty", "A symbol of freedom in New York, USA.", 40.6892, -74.0445),
        Place("Taj Mahal", "A beautiful mausoleum in Agra, India.", 27.1751, 78.0421)
    )

    var selectedPlace by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp )
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(places) { index, (place, description) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = if (index == 0) 40.dp else 8.dp,
                            bottom = 8.dp
                        )
                        .clickable {
                            selectedPlace = place
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = place,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        selectedPlace?.let {
            AzimuthStore.setAzimuth(calculateRelativeAzimuth(lat, lon, places.find { place -> place.name == it }!!.latitude, places.find { place -> place.name == it }!!.longitude))
            Text(
                text = "Selected Place: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val deltaLon = lon2 - lon1
    val y = Math.sin(deltaLon) * Math.cos(lat2)
    val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon)
    var bearing = Math.toDegrees(Math.atan2(y, x)).toFloat()
    bearing = (bearing + 360) % 360
    return bearing
}


fun calculateRelativeAzimuth(userLat: Double, userLon: Double, placeLat: Double, placeLon: Double): Float {
    // Calculate the bearing angle from user's position to the place's position
    val bearing = calculateBearing(Math.toRadians(userLat), Math.toRadians(userLon), Math.toRadians(placeLat), Math.toRadians(placeLon))

    // Adjust bearing to be relative to the user's orientation (if needed)
    // For example, if your device reports azimuth as 0° to 360° clockwise from true north:
    // val relativeAzimuth = (bearing - userAzimuth + 360) % 360

    // For demonstration, return the bearing angle directly
    return bearing
}