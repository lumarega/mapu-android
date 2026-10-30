package com.viajacalu.mapu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viajacalu.mapu.navigation.MapuDestination
import com.viajacalu.mapu.navigation.MapuNavHost
import com.viajacalu.mapu.navigation.MapuNavigationBar
import com.viajacalu.mapu.navigation.TopLevelDestination
import com.viajacalu.mapu.navigation.navigateToTab

@Composable
fun MapuApp(
    startDestination: MapuDestination,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // Null on screens that aren't tabs, like Login, so the bottom bar is hidden there
    val currentTab = TopLevelDestination.entries.firstOrNull { tab ->
        currentBackStackEntry?.destination?.hasRoute(tab.route::class) == true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (currentTab != null) {
                MapuNavigationBar(
                    selectedTab = currentTab,
                    onTabSelected = navController::navigateToTab
                )
            }
        }
    ) { innerPadding ->
        MapuNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
