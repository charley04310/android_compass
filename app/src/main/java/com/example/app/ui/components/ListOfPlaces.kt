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

@Composable
fun FamousPlacesScreen() {
    val places = listOf(
        "Eiffel Tower" to "An iconic symbol of Paris and France.",
        "Great Wall of China" to "An ancient series of walls and fortifications.",
        "Pyramids of Giza" to "Ancient pyramids located in Egypt.",
        "Statue of Liberty" to "A symbol of freedom in New York, USA.",
        "Taj Mahal" to "A beautiful mausoleum in Agra, India."
    )

    var selectedPlace by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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
            Text(
                text = "Selected Place: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
