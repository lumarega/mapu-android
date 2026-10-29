package com.viajacalu.mapu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viajacalu.mapu.feature.auth.presentation.LoginRoute
import com.viajacalu.mapu.feature.home.presentation.HomeScreen

@Composable
fun MapuNavHost(
    startDestination: MapuDestination,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<MapuDestination.Login> {
            LoginRoute(
                onSignedIn = {
                    navController.navigate(MapuDestination.Home) {
                        popUpTo<MapuDestination.Login> { inclusive = true }
                    }
                }
            )
        }

        composable<MapuDestination.Home> {
            HomeScreen()
        }
    }
}
