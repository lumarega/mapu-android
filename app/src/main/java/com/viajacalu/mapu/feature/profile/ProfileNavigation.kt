package com.viajacalu.mapu.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ProfileDestination

fun NavController.navigateToProfile(navOptions: NavOptions? = null) = navigate(ProfileDestination, navOptions)

fun NavGraphBuilder.profileScreen(onSignedOut: () -> Unit) {
    composable<ProfileDestination> {
        ProfileRoute(onSignedOut = onSignedOut)
    }
}
