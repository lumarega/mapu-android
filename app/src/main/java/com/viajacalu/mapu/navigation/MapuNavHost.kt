package com.viajacalu.mapu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.viajacalu.mapu.feature.guides.guidesScreen
import com.viajacalu.mapu.feature.guides.navigateToGuides
import com.viajacalu.mapu.feature.login.LoginDestination
import com.viajacalu.mapu.feature.login.loginScreen
import com.viajacalu.mapu.feature.login.navigateToLogin
import com.viajacalu.mapu.feature.profile.profileScreen

// Each feature adds its own screens to the graph, and this file connects them
@Composable
fun MapuNavHost(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            onSignedIn = {
                navController.navigateToGuides(
                    navOptions { popUpTo<LoginDestination> { inclusive = true } }
                )
            }
        )

        guidesScreen()

        profileScreen(
            onSignedOut = {
                // Clears the whole back stack, so back from Login leaves the app
                navController.navigateToLogin(
                    navOptions { popUpTo(navController.graph.id) { inclusive = true } }
                )
            }
        )
    }
}
