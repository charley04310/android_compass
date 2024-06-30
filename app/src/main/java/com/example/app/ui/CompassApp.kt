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

package com.example.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.app.ui.components.CompassComponent
import com.example.app.ui.components.FamousPlacesScreen
import com.example.app.ui.components.MapComponent
import com.example.app.ui.navigation.ModalNavigationDrawerContent
import com.example.app.ui.navigation.PermanentNavigationDrawerContent
import com.example.app.ui.navigation.AppBottomNavigationBar
import com.example.app.ui.navigation.AppNavigationActions
import com.example.app.ui.navigation.CompassRoute
import com.example.app.ui.navigation.AppTopLevelDestination
import com.example.app.ui.utils.DevicePosture
import com.example.app.ui.utils.AppContentType
import com.example.app.ui.utils.AppNavigationContentPosition
import com.example.app.ui.utils.AppNavigationType
import com.example.app.ui.utils.isBookPosture
import com.example.app.ui.utils.isSeparating
import kotlinx.coroutines.launch

@Composable
fun App(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    azimuth: Float,
    userLatitude: Double,
    userLongitude: Double
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: AppNavigationType

    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = AppNavigationType.NAVIGATION_RAIL

        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                AppNavigationType.NAVIGATION_RAIL
            } else {
                AppNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
        }
        else -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            AppNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            AppNavigationContentPosition.CENTER
        }
        else -> {
            AppNavigationContentPosition.TOP
        }
    }

    AppNavigationWrapper(
        navigationType = navigationType,
        navigationContentPosition = navigationContentPosition,
        azimuth = azimuth,
        userLatitude = userLatitude,
        userLongitude = userLongitude
    )
}

@Composable
private fun AppNavigationWrapper(
    navigationType: AppNavigationType,
    navigationContentPosition: AppNavigationContentPosition,
    azimuth : Float,
    userLatitude: Double,
    userLongitude: Double
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: CompassRoute.EXPLORE

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            )
        }) {
            AppContent(
                navigationType = navigationType,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                azimuth = azimuth,
                userLatitude = userLatitude,
                userLongitude = userLongitude
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            AppContent(
                navigationType = navigationType,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                azimuth = azimuth,
                userLatitude = userLatitude,
                userLongitude = userLongitude
            )
        }
    }
}

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    azimuth: Float,
    userLatitude: Double,
    userLongitude: Double,
    navigationType: AppNavigationType,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (AppTopLevelDestination) -> Unit,
) {
    Row(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            AppNavHost(
                navController = navController,
                modifier = Modifier.weight(1f),
                azimuth = azimuth,
                userLatitude =userLatitude,
                userLongitude = userLongitude
            )
            AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION) {
                AppBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    azimuth: Float,
    userLatitude: Double,
    userLongitude: Double
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CompassRoute.EXPLORE,
    ) {
        composable(CompassRoute.EXPLORE) {
            CompassComponent(azimuth = azimuth)
        }
        composable(CompassRoute.MAP) {
            MapComponent(userLongitude=userLongitude,  userLatitude=userLatitude )
        }
        composable(CompassRoute.APARTMENT) {
            FamousPlacesScreen(userLatitude, userLongitude)
        }
        composable(CompassRoute.FAVORITES) {
            EmptyComingSoon()
        }
    }
}