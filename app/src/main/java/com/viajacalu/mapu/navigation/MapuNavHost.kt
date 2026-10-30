package com.viajacalu.mapu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.viajacalu.mapu.feature.auth.presentation.LoginRoute
import com.viajacalu.mapu.feature.guides.presentation.GuidesScreen
import com.viajacalu.mapu.feature.profile.presentation.ProfileRoute

@Composable
fun MapuNavHost(
    navController: NavHostController,
    startDestination: MapuDestination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<MapuDestination.Login> {
            LoginRoute(
                onSignedIn = {
                    navController.navigate(MapuDestination.Guides) {
                        popUpTo<MapuDestination.Login> { inclusive = true }
                    }
                }
            )
        }

        composable<MapuDestination.Guides> {
            GuidesScreen()
        }

        composable<MapuDestination.Profile> {
            ProfileRoute(
                onSignedOut = {
                    // Clears the whole back stack, so back from Login leaves the app
                    navController.navigate(MapuDestination.Login) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}
