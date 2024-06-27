package com.example.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.mapbox.search.SearchOptions
import com.mapbox.search.SearchSuggestionsCallback
import com.mapbox.search.result.SearchSuggestion

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    userLocation: Point? = null

) {

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var suggestions by remember { mutableStateOf<List<SearchSuggestion>>(emptyList()) }

    val options = SearchOptions(
        limit = 4

    )
    val mapViewportState = remember {
        MapViewportState().apply {
            setCameraOptions {
                zoom(5.0)
                center(Point.fromLngLat(-98.0, 39.5))
                pitch(0.0)
                bearing(0.0)
            }
        }
    }

    userLocation?.let {
        mapViewportState.setCameraOptions {
            center(it)
        }
    }
    val searchEngine = remember {
        SearchEngine.createSearchEngineWithBuiltInDataProviders(
            settings = SearchEngineSettings()
        )
    }


    Box(modifier = modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier.matchParentSize(),
            mapViewportState = mapViewportState,
            style = {
                MapStyle(style = Style.SATELLITE_STREETS)
            }

        )

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = searchText,
                onValueChange = { text ->
                    searchText = text
                    searchEngine.search(
                        searchText.text,
                        options,
                        object : SearchSuggestionsCallback {

                            override fun onError(e: Exception) {
                            }

                            override fun onSuggestions(suggestionList: List<SearchSuggestion>, responseInfo: ResponseInfo) {
                                suggestions = suggestionList // Update suggestions

                            }
                        },
                    )
                    searchText = text
                },
                placeholder = { Text(text = "Search") }
            )

            // Suggestions Dropdown
            if (suggestions.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    suggestions.forEach { suggestion ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchText = TextFieldValue(suggestion.name)
                                    suggestions = emptyList()
                                    // Move map to suggestion location

                                },
                            color = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = suggestion.name
                            )
                        }
                    }
                }
            }
        }
    }
}
