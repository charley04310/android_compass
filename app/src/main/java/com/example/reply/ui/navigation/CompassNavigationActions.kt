/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.reply.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.reply.R

object CompassRoute {
    const val EXPLORE = "Explore"
    const val MAP = "Map"
    const val APARTMENT = "Apartment"
    const val FAVORITES = "Favorites"
}

data class ReplyTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class ReplyNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: ReplyTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    ReplyTopLevelDestination(
        route = CompassRoute.EXPLORE,
        selectedIcon = Icons.Default.Explore,
        unselectedIcon = Icons.Default.Explore,
        iconTextId = R.string.tab_explore
    ),
    ReplyTopLevelDestination(
        route = CompassRoute.MAP,
        selectedIcon = Icons.Default.Map,
        unselectedIcon = Icons.Default.Map,
        iconTextId = R.string.tab_map
    ),
    ReplyTopLevelDestination(
        route = CompassRoute.APARTMENT,
        selectedIcon = Icons.Default.Apartment,
        unselectedIcon = Icons.Default.Apartment,
        iconTextId = R.string.tab_apartment
    ),
    ReplyTopLevelDestination(
        route = CompassRoute.FAVORITES,
        selectedIcon = Icons.Default.Stars,
        unselectedIcon = Icons.Default.Stars,
        iconTextId = R.string.tab_favorites
    )

)
