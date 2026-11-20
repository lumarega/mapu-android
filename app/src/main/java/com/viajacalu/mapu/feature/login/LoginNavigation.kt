package com.viajacalu.mapu.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavController.navigateToLogin(navOptions: NavOptions? = null) = navigate(LoginDestination, navOptions)

fun NavGraphBuilder.loginScreen(onSignedIn: () -> Unit) {
    composable<LoginDestination> {
        LoginRoute(onSignedIn = onSignedIn)
    }
}
