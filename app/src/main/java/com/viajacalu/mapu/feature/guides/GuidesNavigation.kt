package com.viajacalu.mapu.feature.guides

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object GuidesDestination

fun NavController.navigateToGuides(navOptions: NavOptions? = null) = navigate(GuidesDestination, navOptions)

fun NavGraphBuilder.guidesScreen() {
    composable<GuidesDestination> {
        GuidesScreen()
    }
}
